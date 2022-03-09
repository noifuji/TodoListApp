package com.noifuji.todolistapp.ui;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noifuji.todolistapp.R;
import com.noifuji.todolistapp.viewmodel.TaskViewModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class TaskListFragment extends Fragment implements DeleteTaskListener {
    private final static String TAG = "TaskListFragment";
    private RecyclerView mTaskListRecyclerView;
    private TaskAdapter mAdapter;
    private TaskViewModel mTaskViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        mTaskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        mTaskListRecyclerView = (RecyclerView) view.findViewById(R.id.task_list_view);

        mAdapter = new TaskAdapter();
        mAdapter.setDeleteTaskListener(this);
        mTaskListRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onStart() {
        super.onStart();
        mDisposable.add(mTaskViewModel.getTaskList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(taskList -> mAdapter.setData(taskList)
                        , throwable -> Log.e(TAG, "Unable to read tasks.", throwable)));
    }

    @Override
    public void onStop() {
        super.onStop();
        mDisposable.clear();
    }

    @Override
    public void onClickDeleteTask(int position) {
        mDisposable.add(mTaskViewModel.deleteTask(position)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, throwable -> Log.e(TAG, "Unable to delete.", throwable)));
    }
}