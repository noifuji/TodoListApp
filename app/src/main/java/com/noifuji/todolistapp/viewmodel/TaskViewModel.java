package com.noifuji.todolistapp.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;

import com.noifuji.todolistapp.AppComponent;
import com.noifuji.todolistapp.db.TaskDAO;
import com.noifuji.todolistapp.db.TaskEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

/**
 * タスクのデータをデータレイヤーから取得して保持する。
 * UIに対して公開する。
 */
public class TaskViewModel extends AndroidViewModel {
    private TaskDAO mTaskDAO;
    private List<TaskEntity> mTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        mTaskDAO = ((AppComponent)application).getDatabase().taskDAO();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Flowable<List<TaskEntity>> getTaskList() {
        return mTaskDAO.getAll()
                .map(tasks -> {
                    mTasks = tasks;
                    return tasks;
                });
    }

    /**
     *
     * @param task タスクのテキスト
     * @return
     */
    public Completable insertTask(TaskEntity task) {
        return mTaskDAO.insert(task);
    }

    /**
     *
     * @param position 削除したいタスクのリスト内のインデックス
     * @return
     */
    public Completable deleteTask(int position) {
        return mTaskDAO.delete(mTasks.get(position));
    }
}
