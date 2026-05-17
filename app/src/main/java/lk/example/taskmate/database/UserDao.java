package lk.example.taskmate.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import lk.example.taskmate.models.User;

@Dao
public interface UserDao {
    @Insert
    void registerUser(User user);

    @Update
    void updateUser(User user);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    User login(String username, String password);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User getUserByUsername(String username);

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    User getUserById(int userId);
}
