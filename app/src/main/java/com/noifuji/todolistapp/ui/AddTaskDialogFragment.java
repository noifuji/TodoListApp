package com.noifuji.todolistapp.ui;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.noifuji.todolistapp.R;
import com.noifuji.todolistapp.db.TaskEntity;
import com.noifuji.todolistapp.viewmodel.TaskViewModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddTaskDialogFragment extends DialogFragment {
    private final static String TAG = "AddTaskDialogFragment";
    private TaskViewModel mTaskViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mTaskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        return inflater.inflate(R.layout.fragment_add_task_dialog, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.fragment_add_task_dialog, null));

        builder.setMessage("タスクの追加")
                .setPositiveButton("OK", (dialog, id) -> {
                    EditText editText = (EditText) getDialog().findViewById(R.id.add_task_text);
                    CheckBox isImportantCheckBox = (CheckBox) getDialog().findViewById(R.id.task_is_important);

                    if(editText != null) {
                        TaskEntity task = new TaskEntity();
                        task.setId(0);
                        task.setText(editText.getText().toString());
                        task.setIsImportant(isImportantCheckBox.isChecked());
                        mDisposable.add(mTaskViewModel.insertTask(task)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {}, throwable -> Log.e(TAG, "Unable to insert.", throwable)));
                    } else {
                        Log.e(TAG, "No text.");
                    }
                })
        .setNegativeButton("キャンセル", (dialog, id) -> {});


        return builder.create();
    }

    @Override
    public void onStop() {
        super.onStop();
        mDisposable.clear();
    }
}