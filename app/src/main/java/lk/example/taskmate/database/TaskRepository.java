package lk.example.taskmate.database;

import android.app.Application;
import androidx.lifecycle.LiveData;
import lk.example.taskmate.models.Task;
import java.util.List;

public class TaskRepository {
    private final TaskDao taskDao;

    public TaskRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        taskDao = db.taskDao();
    }

    public LiveData<List<Task>> getTasksForUser(int userId) {
        return taskDao.getAllTasksForUser(userId);
    }

    public void insert(Task task) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.insertTask(task);
        });
    }

    public void update(Task task) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.updateTask(task);
        });
    }

    public void delete(Task task) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.deleteTask(task);
        });
    }
}
