package com.example.dodozz.model;

import java.io.Serializable;

import androidx.databinding.ObservableArrayList;

public class TodoList implements Serializable {
    public int id;
    public String title;
    public ObservableArrayList<TaskListItem> tasks;
    public static int count = 0;

    public TodoList(String title, ObservableArrayList<TaskListItem> tasks) {
        count++;
        this.id = count;
        this.title = title;
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return "TodoList{" +
                "title='" + title + '\'' +
                ", tasks=" + tasks +
                '}';
    }
}

