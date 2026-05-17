package lk.example.taskmate.database;

import android.app.Application;
import lk.example.taskmate.models.User;
import java.util.concurrent.ExecutorService;

public class UserRepository {
    private final UserDao userDao;
    private final ExecutorService executor;

    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        userDao = db.userDao();
        executor = AppDatabase.databaseWriteExecutor;
    }

    public void register(User user, RepositoryCallback<Void> callback) {
        executor.execute(() -> {
            try {
                userDao.registerUser(user);
                callback.onSuccess(null);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void login(String username, String password, RepositoryCallback<User> callback) {
        executor.execute(() -> {
            try {
                User user = userDao.login(username, password);
                callback.onSuccess(user);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void getUserByUsername(String username, RepositoryCallback<User> callback) {
        executor.execute(() -> {
            try {
                User user = userDao.getUserByUsername(username);
                callback.onSuccess(user);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void getUserById(int userId, RepositoryCallback<User> callback) {
        executor.execute(() -> {
            try {
                User user = userDao.getUserById(userId);
                callback.onSuccess(user);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void update(User user, RepositoryCallback<Void> callback) {
        executor.execute(() -> {
            try {
                userDao.updateUser(user);
                callback.onSuccess(null);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public interface RepositoryCallback<T> {
        void onSuccess(T result);
        void onError(Exception e);
    }
}
