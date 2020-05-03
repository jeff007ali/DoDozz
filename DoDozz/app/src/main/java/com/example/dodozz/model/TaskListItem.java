package com.example.dodozz.model;

import java.io.Serializable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class TaskListItem extends BaseObservable implements Serializable {
    @Bindable
    public String getTaskItemName() {
        return taskItemName;
    }

    public void setTaskItemName(String taskItemName) {
        this.taskItemName = taskItemName;
    }


    private String taskItemName;

    @Bindable
    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    private boolean completed;

    public TaskListItem(String taskItemName) {
        this.taskItemName = taskItemName;
        this.completed = false;
    }

    public TaskListItem(String taskItemName, boolean completed) {
        this.taskItemName = taskItemName;
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "TaskListItem{" +
                "taskItemName='" + taskItemName + '\'' +
                ", completed=" + completed +
                '}';
    }
}
