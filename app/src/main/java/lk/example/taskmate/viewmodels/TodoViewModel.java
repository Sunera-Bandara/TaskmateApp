package lk.example.taskmate.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import lk.example.taskmate.models.Task;
import lk.example.taskmate.database.TaskRepository;
import java.util.List;

public class TodoViewModel extends AndroidViewModel {
    private final TaskRepository repository;

    public TodoViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
    }

    public LiveData<List<Task>> getTasksForUser(int userId) {
        return repository.getTasksForUser(userId);
    }

    public void insert(Task task) {
        new Thread(() -> repository.insert(task)).start();
    }

    public void update(Task task) {
        new Thread(() -> repository.update(task)).start();
    }

    public void delete(Task task) {
        new Thread(() -> repository.delete(task)).start();
    }
}
