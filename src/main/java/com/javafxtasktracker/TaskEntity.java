package com.javafxtasktracker;

import java.io.Serializable;
import java.time.LocalDate;

public class TaskEntity implements Serializable {

    private final String name;
    private boolean isReady = false;
    private LocalDate date;
    private final String reasonDateChange;
    private boolean outOfTime = false;
    private final int maxGrade;
    private State state = State.in_progress;
    private String status = "";

    public TaskEntity(
            String name_,
            LocalDate date_,
            int maxGrade_,
            String reasonDateChange_
    ) {
        name = name_;
        maxGrade = maxGrade_;
        date = date_;
        reasonDateChange = reasonDateChange_;
    }


    public boolean isOutOfTime() {
        return outOfTime;
    }

    public void setOutOfTime(boolean outOfTime) {
        this.outOfTime = outOfTime;
    }

    public String getReasonDateChange() {
        return reasonDateChange;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public int getMaxGrade() {
        return maxGrade;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isReady() {
        return isReady;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setStatus(String comment, int g, int mg) {
        this.status = comment + " " + g + "/" + mg;
    }

    public String getStatus() {
        return status;
    }

    public enum State {
        in_progress(""), ok("Выполено в срок"), not_quit_ok("Выполнено не в срок"), no_ok("Не выполнено в срок");
        final String value;

        State(String c) {
            value = c;
        }

        public String getValue() {
            return value;
        }
    }
}

