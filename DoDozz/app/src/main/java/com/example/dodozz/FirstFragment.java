package com.example.dodozz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dodozz.databinding.FragmentFirstBinding;
import com.example.dodozz.databinding.ItemMainTodoListBinding;
import com.example.dodozz.databinding.ItemTaskListBinding;
import com.example.dodozz.model.TaskListItem;
import com.example.dodozz.model.TodoList;
import com.github.nitrico.lastadapter.Holder;
import com.github.nitrico.lastadapter.ItemType;
import com.github.nitrico.lastadapter.LastAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
         binding = DataBindingUtil.inflate(inflater, R.layout.fragment_first, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = binding.toDoList;
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

//        for (int i = 0; i < 20; i++) {
//            MainActivity.todoItems.add(new TodoList("list" + i, new ObservableArrayList<TaskListItem>()));
//        }

        ItemType<ItemMainTodoListBinding> mainTodoList = new ItemType<ItemMainTodoListBinding>(R.layout.item_main_todo_list) {
            @Override
            public void onBind(@NotNull final Holder<ItemMainTodoListBinding> holder) {
                holder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("todoList", holder.getBinding().getItem());

                        NavHostFragment.findNavController(FirstFragment.this)
                                .navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
                    }
                });
            }
        };

        // specify an adapter (see also next example)

        new LastAdapter(StorageHelper.getTodoList(getActivity()), BR.item)
                .map(TodoList.class, mainTodoList)
                .into(recyclerView);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }
}
