package lk.example.taskmate.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import lk.example.taskmate.models.Task;
import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM tasks WHERE userId = :userId ORDER BY id DESC")
    LiveData<List<Task>> getAllTasksForUser(int userId);

    @Insert
    void insertTask(Task task);

    @Update
    void updateTask(Task task);

    @Delete
    void deleteTask(Task task);
}
