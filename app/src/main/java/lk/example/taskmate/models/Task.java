package lk.example.taskmate.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private boolean isCompleted;
    private int userId;

    public Task(String title, int userId) {
        this.title = title;
        this.userId = userId;
        this.isCompleted = false;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}
