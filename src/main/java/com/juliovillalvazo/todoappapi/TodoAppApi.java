package com.juliovillalvazo.todoappapi;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
public class TodoAppApi {
    private List<TodoModel> todos = new ArrayList<>();

    @GetMapping("/todos")
    public List<TodoModel> readTodos(@RequestBody(required = false) Map<String, String> requestBody) {
        return todos;
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
        TodoModel newTodo = new TodoModel(id, name, priority, state, formattedDueDate, formattedDoneDate);

        todos.add(newTodo);

        return newTodo;
    }

    @PutMapping("/todos/{id}")
    public TodoModel updateTodo(@PathVariable String id, @RequestBody TodoModel requestBody) {
        UUID formattedId = UUID.fromString(id);

        String name = requestBody.getName();
        String priority = requestBody.getPriority();
        String state = requestBody.getState();
        Optional<Long> dueDate = requestBody.getDueDate();
        Optional<Long> doneDate = requestBody.getDoneDate();

        for(TodoModel todo : todos) {
            if(todo.getId().equals(formattedId)) {
                todo.setName(name);
                todo.setDoneDate(doneDate);
                todo.setDueDate(dueDate);
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
}

