package com.juliovillalvazo.todoappapi.Misc;

import com.juliovillalvazo.todoappapi.models.TodoModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.Optional;

public class SortHandler {
    public Comparator<TodoModel> getSortComparator(String sortingType) {
        switch(sortingType) {
            case "priority-asc":
                return Comparator.comparing(this::getPriorityOrder);
            case "priority-desc":
                return Comparator.comparing(this::getPriorityOrder).reversed();
            case "dueDate-desc":
                return getDueDateComparator(false);
            case "dueDate-asc":
                return getDueDateComparator(true);
            case "priority-asc-dueDate-asc":
                return Comparator.comparing(this::getPriorityOrder)
                        .thenComparing(getDueDateComparator(true));
            case "priority-desc-dueDate-desc":
                return Comparator.comparing(this::getPriorityOrder).reversed()
                        .thenComparing(getDueDateComparator(false));
            case "priority-desc-dueDate-asc":
                return Comparator.comparing(this::getPriorityOrder).reversed()
                        .thenComparing(getDueDateComparator(true));
            case "priority-asc-dueDate-desc":
                return Comparator.comparing(this::getPriorityOrder)
                        .thenComparing(getDueDateComparator(false));
            case "dueDate-asc-priority-asc":
                return getDueDateComparator(true).thenComparing(this::getPriorityOrder);
            case "dueDate-desc-priority-desc":
                return getDueDateComparator(false).thenComparing(this::getPriorityOrder).reversed();
            case "dueDate-desc-priority-asc":
                return getDueDateComparator(true).thenComparing(this::getPriorityOrder).reversed();
            case "dueDate-asc-priority-desc":
                return getDueDateComparator(false).thenComparing(this::getPriorityOrder);
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private Comparator<TodoModel> getDueDateComparator(boolean asc) {
        return (todo1, todo2) -> {
            Optional<Long> dueDate1 = todo1.getDueDate();
            Optional<Long> dueDate2 = todo2.getDueDate();

            if(dueDate1.isEmpty() && dueDate2.isEmpty()) return 0;
            else if(dueDate1.isEmpty()) return asc ? 1 : -1;
            else if(dueDate2.isEmpty()) return asc ? -1 : 1;

            int comparisonResult = Long.compare(dueDate1.get(), dueDate2.get());
            if (comparisonResult == 0) return 0;

            if (!asc) {
                return comparisonResult * -1;
            }
            return comparisonResult;
        };
    }

    private int getPriorityOrder(TodoModel todo) {
        String priority = todo.getPriority();
        switch (priority) {
            case "high":
                return 1;
            case "medium":
                return 2;
            case "low":
                return 3;
            default:
                return 0;
        }
    }
}
