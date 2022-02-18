package com.noifuji.todolistapp;

import android.app.Application;
import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.noifuji.todolistapp.db.AppDatabase;
import com.noifuji.todolistapp.db.TaskDAO;
import com.noifuji.todolistapp.db.TaskEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DatabaseReadWriteTest {
    private TaskDAO taskDAO;
    private AppDatabase db;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        taskDAO = db.taskDAO();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void insertTest() {
        TaskEntity task = new TaskEntity();
        task.setId(0);
        task.setText("aaa");
        taskDAO.insert(task).blockingAwait();

        taskDAO.getAll()
                .test()
                .assertValue(tasks -> tasks.size() == 1);
    }

    @Test
    public void deleteTest() {
        TaskEntity task = new TaskEntity();
        task.setId(1);
        task.setText("aaa");
        taskDAO.insert(task).blockingAwait();

        taskDAO.delete(task).blockingAwait();

        taskDAO.getAll()
                .test()
                .assertValue(tasks -> tasks.size() == 0);
    }
}