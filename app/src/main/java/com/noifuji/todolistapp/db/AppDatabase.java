package com.noifuji.todolistapp.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * データベースを管理する
 */
@Database(entities = {TaskEntity.class}, version = 2, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase sInstance;
    public static final String DATABASE_NAME = "mydb";

    public abstract TaskDAO taskDAO();

    public static AppDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                sInstance = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                        .addMigrations(MIGRATION_1_2)
                        .build();
            }
        }

        return sInstance;
    }

    public static final Migration MIGRATION_1_2 = new Migration(1,2) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE tasks "
            + "ADD COLUMN is_important INTEGER NOT NULL DEFAULT 0");
        }
    };
}
