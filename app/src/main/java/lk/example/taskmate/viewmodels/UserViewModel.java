package lk.example.taskmate.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import lk.example.taskmate.models.User;
import lk.example.taskmate.database.UserRepository;

public class UserViewModel extends AndroidViewModel {
    private final UserRepository repository;
    private final MutableLiveData<User> loginResult = new MutableLiveData<>();
    private final MutableLiveData<String> errorResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> registrationSuccess = new MutableLiveData<>();

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
    }

    public LiveData<User> getLoginResult() {
        return loginResult;
    }

    public LiveData<String> getErrorResult() {
        return errorResult;
    }

    public LiveData<Boolean> getRegistrationSuccess() {
        return registrationSuccess;
    }

    public void login(String username, String password) {
        repository.login(username, password, new UserRepository.RepositoryCallback<User>() {
            @Override
            public void onSuccess(User user) {
                loginResult.postValue(user);
            }

            @Override
            public void onError(Exception e) {
                errorResult.postValue(e.getMessage());
            }
        });
    }

    public void register(User user) {
        repository.getUserByUsername(user.getUsername(), new UserRepository.RepositoryCallback<User>() {
            @Override
            public void onSuccess(User existingUser) {
                if (existingUser != null) {
                    errorResult.postValue("Username already taken");
                } else {
                    repository.register(user, new UserRepository.RepositoryCallback<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            registrationSuccess.postValue(true);
                        }

                        @Override
                        public void onError(Exception e) {
                            errorResult.postValue(e.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                errorResult.postValue(e.getMessage());
            }
        });
    }

    public void updateProfile(User user, UserRepository.RepositoryCallback<Void> callback) {
        repository.update(user, callback);
    }

    public void getUserById(int userId, UserRepository.RepositoryCallback<User> callback) {
        repository.getUserById(userId, callback);
    }
}
