package lk.example.taskmate.adapters;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import lk.example.taskmate.databinding.ItemTaskBinding;
import lk.example.taskmate.models.Task;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> tasks;
    private final OnTaskClickListener listener;

    public interface OnTaskClickListener {
        void onTaskCheckChanged(Task task);
        void onDeleteClick(Task task);
        void onEditClick(Task task);
    }

    public TaskAdapter(List<Task> tasks, OnTaskClickListener listener) {
        this.tasks = tasks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTaskBinding binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TaskViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.bind(task, listener);
    }

    @Override
    public int getItemCount() {
        return tasks == null ? 0 : tasks.size();
    }

    public void setTasks(List<Task> newTasks) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return tasks == null ? 0 : tasks.size();
            }

            @Override
            public int getNewListSize() {
                return newTasks == null ? 0 : newTasks.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return tasks.get(oldItemPosition).getId() == newTasks.get(newItemPosition).getId();
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                Task oldTask = tasks.get(oldItemPosition);
                Task newTask = newTasks.get(newItemPosition);
                return oldTask.getTitle().equals(newTask.getTitle()) &&
                       oldTask.isCompleted() == newTask.isCompleted();
            }
        });
        this.tasks = newTasks;
        diffResult.dispatchUpdatesTo(this);
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        private final ItemTaskBinding binding;

        public TaskViewHolder(@NonNull ItemTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Task task, OnTaskClickListener listener) {
            binding.taskText.setText(task.getTitle());
            binding.taskCheckbox.setChecked(task.isCompleted());
            
            updateStrikeThrough(binding.taskText, task.isCompleted());

            binding.taskCheckbox.setOnClickListener(v -> {
                task.setCompleted(binding.taskCheckbox.isChecked());
                updateStrikeThrough(binding.taskText, task.isCompleted());
                listener.onTaskCheckChanged(task);
            });

            binding.deleteButton.setOnClickListener(v -> listener.onDeleteClick(task));
            binding.editButton.setOnClickListener(v -> listener.onEditClick(task));
        }

        private void updateStrikeThrough(TextView textView, boolean isCompleted) {
            if (isCompleted) {
                textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                textView.setTextColor(0xFF9E9E9E);
            } else {
                textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                textView.setTextColor(0xFF1A1C1E);
            }
        }
    }
}
