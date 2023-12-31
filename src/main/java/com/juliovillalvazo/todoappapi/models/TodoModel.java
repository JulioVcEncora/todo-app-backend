package com.juliovillalvazo.todoappapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public class TodoModel {
    private UUID id;
    @Size(max = 120)
    private String name;
    @NotEmpty
    private String priority;
    @NotEmpty
    private String state;
    private Optional<Long> dueDate;
    private Optional<Long> doneDate;
    @JsonIgnore
    private long createdDate;

    public TodoModel() {
    }

    public TodoModel(UUID id, String name, String priority, String state, Optional<Long> dueDate, Optional<Long> doneDate, long createdDate) {
        this.id = id;
        this.name = name;
        this.priority = priority;
        this.state = state;
        this.dueDate = dueDate;
        this.doneDate = doneDate;
        this.createdDate = createdDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Optional<Long> getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(Optional<Long> dueDate) {
        this.dueDate = dueDate;
    }

    public Optional<Long> getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(Optional<Long> finishedDate) {
        this.doneDate = finishedDate;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }
}
