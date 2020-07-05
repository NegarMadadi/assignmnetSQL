package org.example.entity;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

public class Todo {

    private  Integer todo_id;
    private String title;
    private String description;
    private LocalDate deadline;
    private boolean done;
    private Person assignee;


    public Todo(Integer todo_id, String title, String description, LocalDate deadline, boolean done, Person assignee) {
        this.todo_id = todo_id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.done = done;
        this.assignee = assignee;
    }

    public Todo(String title, String description, LocalDate deadline, boolean done, Person assignee) {
        this(null, title, description, deadline, done, assignee);
    }

    public Todo(String title, String description, LocalDate deadline, boolean done) {
        this(title, description, deadline, done, null);
    }

    public int getTodo_id() {
        return todo_id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Person getAssignee() {
        return assignee;
    }

    public void setAssignee(Person assignee) {
        this.assignee = assignee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return getTodo_id() == todo.getTodo_id() &&
                isDone() == todo.isDone() &&
                Objects.equals(getTitle(), todo.getTitle()) &&
                Objects.equals(getDescription(), todo.getDescription()) &&
                Objects.equals(getDeadline(), todo.getDeadline());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTodo_id(), getTitle(), getDescription(), getDeadline(), isDone());
    }

    @Override
    public String toString() {
        return "Todo{" +
                "todo_id=" + todo_id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", deadline=" + deadline +
                ", done=" + done +
                ", assignee=" + assignee +
                '}';
    }
}


