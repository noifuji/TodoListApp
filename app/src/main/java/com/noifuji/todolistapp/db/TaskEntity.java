package com.noifuji.todolistapp.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * タスクのエンティティ
 */
@Entity(tableName = "tasks")
public class TaskEntity {
    @PrimaryKey(autoGenerate = true)
    private int mId;

    @ColumnInfo(name = "text")
    private String mText;

    @ColumnInfo(name = "is_important")
    private boolean mIsImportant;

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

    public boolean isImportant() {
        return mIsImportant;
    }

    public void setIsImportant(boolean mIsImportant) {
        this.mIsImportant = mIsImportant;
    }
}
