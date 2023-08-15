package com.juliovillalvazo.todoappapi;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
public class TodoAppApi {
    private List<TodoModel> todos = new CopyOnWriteArrayList<>();

    @GetMapping("/todos")
    public ResponseEntity<Page<TodoModel>> getPaginatedTodos(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) Optional<Long> dueDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        int startIndex = page * pageSize;
        if (name == null && priority == null && state == null && dueDate.isEmpty()) {
            // Calculate the range of elements for the requested page
            int endIndex = Math.min(startIndex + pageSize, todos.size());

            List<TodoModel> paginatedTodos = todos.subList(startIndex, endIndex);

            // Create a Page object and return it
            Page<TodoModel> pageResult = new PageImpl<>(paginatedTodos, PageRequest.of(page, pageSize), todos.size());
        }

        if (priority != null && !priority.equals("high") && !priority.equals("medium") && !priority.equals("low") && !priority.equals("all")) {
            throw new InvalidParamsExceptionHandler("Priority can only be high, medium or low!");
        }

        if (state != null && !state.equals("done") && !state.equals("undone") && !state.equals("all")) {
            throw new InvalidParamsExceptionHandler("State can only be done or undone!");
        }

        List<TodoModel> filteredTodos = new ArrayList<>(todos);

        if (name != null) {
            filteredTodos.removeIf(task -> !task.getName().contains(name));
        }

        if (priority != null && !priority.equals("all")) {
            filteredTodos.removeIf(task -> !task.getPriority().equals(priority));
        }

        if (state != null && !state.equals("all")) {
            filteredTodos.removeIf(task -> !task.getState().equals(state));
        }

        if (dueDate.isPresent()) {
            filteredTodos.removeIf(task -> !task.getDueDate().equals(dueDate));
        }

        // Calculate the range of elements for the requested page
        int endIndex = Math.min(startIndex + pageSize, filteredTodos.size());

        List<TodoModel> paginatedTodos = filteredTodos.subList(startIndex, endIndex);

        // Create a Page object and return it
        Page<TodoModel> pageResult = new PageImpl<>(paginatedTodos, PageRequest.of(page, pageSize), filteredTodos.size());

        return ResponseEntity.ok(pageResult);
    }


    @PostMapping("/todos")
    public TodoModel createTodo(@Valid @RequestBody Map<String, String> requestBody) {

        String name = requestBody.getOrDefault("name", "");
        String priority = requestBody.getOrDefault("priority", "");
        String dueDate = requestBody.getOrDefault("dueDate", "");
        String state = requestBody.getOrDefault("state", "");
        String doneDate = requestBody.getOrDefault("doneDate", "");

        if(name.length() > 120) {
            throw new InvalidParamsExceptionHandler("Name must be less than 130 characters!");
        }

        if(!(priority.equals("high") || priority.equals("low") || priority.equals("medium"))) {
            throw new InvalidParamsExceptionHandler("Priority can only low, high or medium!");
        }

        if(!(state.equals("done") || state.equals("undone"))) {
            throw new InvalidParamsExceptionHandler("State can only be done or undone!");
        }

        Optional<Long> formattedDueDate;
        Optional<Long> formattedDoneDate;

        if(!dueDate.isEmpty()) {
            formattedDueDate = Optional.of(Long.parseLong(dueDate));
        } else {
            formattedDueDate = Optional.empty();
        }

        if(!doneDate.isEmpty()) {
            formattedDoneDate = Optional.of(Long.parseLong(doneDate));
        } else {
            formattedDoneDate = Optional.empty();
        }

        if(state.equals("done") && formattedDoneDate.isEmpty()) {
            formattedDoneDate = Optional.of(System.currentTimeMillis());
        }

        UUID id = UUID.randomUUID();
        TodoModel newTodo = new TodoModel(id, name, priority, state, formattedDueDate, formattedDoneDate, System.currentTimeMillis());

        todos.add(newTodo);

        return newTodo;
    }
    @CrossOrigin(origins = "http://localhost:5173")
    @PutMapping("/todos/{id}")
    public TodoModel updateTodo(@PathVariable String id, @RequestBody TodoModel requestBody) {
        UUID formattedId = UUID.fromString(id);

        String name = requestBody.getName();
        String priority = requestBody.getPriority();
        String state = requestBody.getState();
        Optional<Long> dueDate = requestBody.getDueDate();

        for(TodoModel todo : todos) {
            if(todo.getId().equals(formattedId)) {
                todo.setName(name);
                todo.setDueDate(dueDate);
                if(state.equals("undone")) {
                    todo.setDoneDate(Optional.empty());
                }
                if(state.equals("done") && todo.getDoneDate().isEmpty()) {
                    todo.setDoneDate(Optional.of(System.currentTimeMillis()));
                }
                todo.setState(state);
                todo.setPriority(priority);
                return todo;
            }
        }

        throw new NotFoundExceptionHandler("Could not find that To Do!");
    }

    @PostMapping("/todos/{id}/done")
    public TodoModel markAsDone(@PathVariable String id) {
        UUID formattedId = UUID.fromString(id);

        for (TodoModel todo : todos) {
            if(todo.getId().equals(formattedId) && !todo.getState().equals("done")) {
                todo.setState("done");
                todo.setDoneDate(Optional.of(System.currentTimeMillis()));
                return todo;
            }
            if(todo.getId().equals(formattedId) && todo.getState().equals("done")) {
                throw new ResponseStatusException(HttpStatus.ACCEPTED, "done");

            }
        }

        throw new NotFoundExceptionHandler("Could not find that To Do!");
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PutMapping("/todos/{id}/undone")
    public TodoModel markAsUndone(@PathVariable String id) {
        UUID formattedId = UUID.fromString(id);

        for(TodoModel todo : todos) {
            if(todo.getId().equals(formattedId) && todo.getState().equals("done")) {
                todo.setState("undone");
                todo.setDoneDate(Optional.empty());
                return todo;
            }
            if(todo.getId().equals(formattedId) && !todo.getState().equals("done")) {
                throw new ResponseStatusException(HttpStatus.OK, "done");
            }
        }

        throw new NotFoundExceptionHandler("Could not find that To Do!");
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/todos/metrics")
    public MetricsModel getMetrics() {
        long generalSum = 0;
        long generalElements = 0;
        long generalAverageTime = 0;

        long lowSum = 0;
        long lowElements = 0;
        long lowAverage = 0;

        long mediumSum = 0;
        long mediumElements = 0;
        long mediumAverage = 0;

        long highSum = 0;
        long highElements = 0;
        long highAverage = 0;

        for(TodoModel todo : todos) {
            if(todo.getState().equals("done") && todo.getDoneDate().isPresent()) {
                generalSum += todo.getDoneDate().get() - todo.getCreatedDate();
                generalElements++;

                if(todo.getPriority().equals("low")) {
                    lowSum += todo.getDoneDate().get() - todo.getCreatedDate();
                    lowElements++;
                }

                if(todo.getPriority().equals("medium")) {
                    mediumSum += todo.getDoneDate().get() - todo.getCreatedDate();
                    mediumElements++;
                }

                if(todo.getPriority().equals("high")) {
                    highSum += todo.getDoneDate().get() - todo.getCreatedDate();
                    highElements++;
                }
            }
        }

        if(generalElements > 0) {
            generalAverageTime = generalSum / generalElements;
        }

        if(lowElements > 0) {
            lowAverage = lowSum / lowElements;
        }

        if(mediumElements > 0) {
            mediumAverage = mediumSum / mediumElements;
        }

        if(highElements > 0) {
            highAverage = highSum / highElements;
        }

        return new MetricsModel(generalAverageTime, lowAverage, mediumAverage, highAverage);
    }
}

