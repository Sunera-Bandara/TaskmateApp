package lk.example.taskmate.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lk.example.taskmate.R;
import lk.example.taskmate.adapters.TaskAdapter;
import lk.example.taskmate.databinding.ActivityTodoBinding;
import lk.example.taskmate.databinding.DialogAddTaskStyledBinding;
import lk.example.taskmate.models.Task;
import lk.example.taskmate.viewmodels.TodoViewModel;

public class TodoActivity extends AppCompatActivity implements TaskAdapter.OnTaskClickListener {

    private ActivityTodoBinding binding;
    private DialogAddTaskStyledBinding dialogBinding;
    private List<Task> taskList = new ArrayList<>();
    private TaskAdapter adapter;
    private TodoViewModel viewModel;
    private Task taskToEdit = null;

    private SharedPreferences prefs;
    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_ID = "user_id";
    private static final String KEY_NAME = "user_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTodoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.dialogBinding = binding.includedDialog;
        viewModel = new ViewModelProvider(this).get(TodoViewModel.class);
        prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        binding.profileButton.setOnClickListener(v ->
                startActivity(new Intent(this, UserInfoActivity.class)));
        binding.infoButton.setOnClickListener(v ->
                startActivity(new Intent(this, DevInfoActivity.class)));

        adapter = new TaskAdapter(taskList, this);
        binding.taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.taskRecyclerView.setAdapter(adapter);

        int userId = prefs.getInt(KEY_ID, -1);
        viewModel.getTasksForUser(userId).observe(this, tasks -> {
            this.taskList = tasks;
            adapter.setTasks(tasks);
            updateStats();
        });

        binding.fabAddCard.setOnClickListener(v -> {
            taskToEdit = null;
            dialogBinding.todoInput.setText("");
            dialogBinding.dialogTitle.setText(R.string.new_task);
            dialogBinding.dialogOk.setText(R.string.create);
            binding.dialogOverlay.setVisibility(View.VISIBLE);
        });

        dialogBinding.dialogCancel.setOnClickListener(v -> binding.dialogOverlay.setVisibility(View.GONE));

        dialogBinding.dialogOk.setOnClickListener(v -> {
            String taskTitle = dialogBinding.todoInput.getText().toString().trim();
            if (!taskTitle.isEmpty()) {
                if (taskToEdit == null) {
                    Task newTask = new Task(taskTitle, userId);
                    viewModel.insert(newTask);
                    Toast.makeText(this, "Task added", Toast.LENGTH_SHORT).show();
                } else {
                    // Create a copy to update so DiffUtil detects the change
                    Task updatedTask = new Task(taskTitle, taskToEdit.getUserId());
                    updatedTask.setId(taskToEdit.getId());
                    updatedTask.setCompleted(taskToEdit.isCompleted());
                    
                    viewModel.update(updatedTask);
                    Toast.makeText(this, "Task updated", Toast.LENGTH_SHORT).show();
                }
            }
            binding.dialogOverlay.setVisibility(View.GONE);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateHeader();
    }

    private void updateHeader() {
        String name = prefs.getString(KEY_NAME, getString(R.string.default_user_name));
        binding.headerUserName.setText(name);

        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String greeting;
        if (hour >= 0 && hour < 12) {
            greeting = "Good morning,";
        } else if (hour >= 12 && hour < 16) {
            greeting = "Good afternoon,";
        } else if (hour >= 16 && hour < 21) {
            greeting = "Good evening,";
        } else {
            greeting = "Good night,";
        }
        binding.greetingText.setText(greeting);
    }

    private void updateStats() {
        int total = taskList.size();
        int done = 0;
        for (Task t : taskList) {
            if (t.isCompleted()) done++;
        }
        int pending = total - done;

        binding.totalTasksCount.setText(String.valueOf(total));
        binding.doneTasksCount.setText(String.valueOf(done));
        binding.pendingTasksCount.setText(String.valueOf(pending));

        String tasksText = getResources().getQuantityString(R.plurals.tasks_count, total, total);
        binding.taskCountChip.setText(tasksText);

        if (total == 0) {
            binding.emptyStateView.setVisibility(View.VISIBLE);
            binding.taskRecyclerView.setVisibility(View.GONE);
        } else {
            binding.emptyStateView.setVisibility(View.GONE);
            binding.taskRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onTaskCheckChanged(Task task) {
        viewModel.update(task);
    }

    @Override
    public void onDeleteClick(Task task) {
        viewModel.delete(task);
        Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEditClick(Task task) {
        taskToEdit = task;
        dialogBinding.todoInput.setText(task.getTitle());
        dialogBinding.dialogTitle.setText(R.string.edit_task);
        dialogBinding.dialogOk.setText(R.string.update);
        binding.dialogOverlay.setVisibility(View.VISIBLE);
    }
}
