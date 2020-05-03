package com.example.dodozz;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.dodozz.model.TodoList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.databinding.ObservableArrayList;

public class StorageHelper {
    private static final String todoListStorageKey = "todoListStorageKey";

    public static ObservableArrayList<TodoList> getTodoList(Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String todoListString = sharedPref.getString(todoListStorageKey, null);
        ObservableArrayList<TodoList> todoListFromStorage = new Gson().fromJson(todoListString, new TypeToken<ObservableArrayList<TodoList>>(){}.getType());
        if (todoListFromStorage == null) {
            todoListFromStorage = new ObservableArrayList<>();
        }
        return todoListFromStorage;
    }

    public static void setTodoList(Activity activity, ObservableArrayList<TodoList> todoLists) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String todoListString = new Gson().toJson(todoLists);
        editor.putString(todoListStorageKey, todoListString);
        editor.commit();
    }
}
