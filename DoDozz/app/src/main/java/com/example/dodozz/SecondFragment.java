package com.example.dodozz;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dodozz.databinding.FragmentSecondBinding;
import com.example.dodozz.databinding.ItemTaskListBinding;
import com.example.dodozz.model.TaskListItem;
import com.example.dodozz.model.TodoList;
import com.github.nitrico.lastadapter.Holder;
import com.github.nitrico.lastadapter.ItemType;
import com.github.nitrico.lastadapter.LastAdapter;

import org.jetbrains.annotations.NotNull;

import java.io.ObjectStreamException;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.ContentValues.TAG;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private ObservableArrayList<TaskListItem> taskListItems = new ObservableArrayList<>();
    private ObservableArrayList<TaskListItem> completedTaskListItems = new ObservableArrayList<>();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_second, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final RecyclerView taskListRecyclerView = binding.taskList;
        taskListRecyclerView.setHasFixedSize(true);

        final RecyclerView completedTaskListRecyclerView = binding.completedTaskList;
        completedTaskListRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        taskListRecyclerView.setLayoutManager(layoutManager1);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        completedTaskListRecyclerView.setLayoutManager(layoutManager2);

        ItemType<ItemTaskListBinding> taskList = new ItemType<ItemTaskListBinding>(R.layout.item_task_list) {
            @Override
            public void onBind(@NotNull final Holder<ItemTaskListBinding> holder) {
                holder.getBinding().btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        taskListItems.remove(holder.getAdapterPosition());
                        binding.taskList.requestLayout();

                    }
                });

                holder.getBinding().chkbxTaskItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TaskListItem item = holder.getBinding().getItem();
                        boolean isChecked = holder.getBinding().chkbxTaskItem.isChecked();
                        item.setCompleted(isChecked);
                        if (isChecked) {
                            taskListItems.remove(item);
                            completedTaskListItems.add(item);
                        }
                        else {
                            completedTaskListItems.remove(item);
                            taskListItems.add(item);
                        }

                        binding.taskList.post(new Runnable() {
                            @Override
                            public void run() {
                                binding.taskList.requestLayout();
                            }
                        });
                        binding.completedTaskList.post(new Runnable() {
                            @Override
                            public void run() {
                                binding.completedTaskList.requestLayout();
                            }
                        });

                    }
                });
            }

        };

        ItemType<ItemTaskListBinding> completedTaskList = new ItemType<ItemTaskListBinding>(R.layout.item_task_list) {
            @Override
            public void onBind(@NotNull final Holder<ItemTaskListBinding> holder) {
                holder.getBinding().btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        completedTaskListItems.remove(holder.getAdapterPosition());
                        binding.completedTaskList.requestLayout();

                    }
                });

                holder.getBinding().txtTaskItem.setPaintFlags(holder.getBinding().txtTaskItem.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                holder.getBinding().chkbxTaskItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TaskListItem item = holder.getBinding().getItem();
                        boolean isChecked = holder.getBinding().chkbxTaskItem.isChecked();
                        item.setCompleted(isChecked);
                        if (isChecked) {
                            taskListItems.remove(item);
                            completedTaskListItems.add(item);
                        }
                        else {
                            completedTaskListItems.remove(item);
                            taskListItems.add(item);
                        }

                        binding.taskList.post(new Runnable() {
                            @Override
                            public void run() {
                                binding.taskList.requestLayout();
                            }
                        });
                        binding.completedTaskList.post(new Runnable() {
                            @Override
                            public void run() {
                                binding.completedTaskList.requestLayout();
                            }
                        });
                    }
                });
            }
        };

        // to initialize adapter even if there is no arguments, because we need this when we add new task list item
        new LastAdapter(taskListItems, BR.item)
                .map(TaskListItem.class, taskList)
                .into(taskListRecyclerView);

        new LastAdapter(completedTaskListItems, BR.item)
                .map(TaskListItem.class, completedTaskList)
                .into(completedTaskListRecyclerView);

        int id = 0;
        if (getArguments() != null) {

            TodoList todoListFromArgument = ((TodoList) getArguments().getSerializable("todoList"));
            // if we get arguments then we will add this data in global variable taskListItem
            id = todoListFromArgument.id;
            for (TaskListItem task : todoListFromArgument.tasks) {
                if (task.isCompleted()) {
                    completedTaskListItems.add(task);
                }
                else {
                    taskListItems.add(task);
                }
            }

            binding.taskTitle.setText(todoListFromArgument.title);
        }

        final int finalId = id;
        binding.doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.taskTitle.getText().toString().length() == 0) {
                    Toast.makeText(getContext(), "Please enter title", Toast.LENGTH_SHORT).show();
                }
                else {
                    TodoList newTodoList = new TodoList(binding.taskTitle.getText().toString(), taskListItems);
                    ObservableArrayList<TodoList> mainTodoList = StorageHelper.getTodoList(getActivity());
                    if (finalId != 0) {
                        Log.d(TAG, "onClick: storage" + mainTodoList.toString());

//                        int index = mainTodoList.indexOf(finalTodoListFromArgument);
                        int index = -1;
                        for (TodoList todo: mainTodoList) {
                            if (todo.id == finalId) {
                                index = mainTodoList.indexOf(todo);
                                break;
                            }
                        }
                        if (index >= 0) {
                            mainTodoList.set(index, newTodoList);
                            StorageHelper.setTodoList(getActivity(), mainTodoList);
                        }
                    }
                    else {
                        mainTodoList.add(newTodoList);
                        StorageHelper.setTodoList(getActivity(), mainTodoList);
                    }

                    NavHostFragment.findNavController(SecondFragment.this)
                            .navigate(R.id.action_SecondFragment_to_FirstFragment);
                }
            }
        });

        binding.loutAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskListItems.add(new TaskListItem(""));
                binding.taskList.requestLayout();
                binding.loutAddItem.setVisibility(View.GONE);
                binding.taskList.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.taskList.getLayoutManager().findViewByPosition(taskListItems.size() - 1).findViewById(R.id.txt_taskItem).requestFocus();
                    }
                });

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.loutAddItem.setVisibility(View.VISIBLE);
                    }
                }, 1000);
            }
        });

    }
}
