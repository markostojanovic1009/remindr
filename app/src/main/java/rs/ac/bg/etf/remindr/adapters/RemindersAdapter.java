package rs.ac.bg.etf.remindr.adapters;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import rs.ac.bg.etf.remindr.databinding.RemindersListItemBinding;
import rs.ac.bg.etf.remindr.models.Reminder;

public class RemindersAdapter extends ListAdapter<Reminder, RecyclerView.ViewHolder> {

    public static class ReminderDiffCallback extends DiffUtil.ItemCallback<Reminder>
    {

        @Override
        public boolean areItemsTheSame(@NonNull Reminder oldItem, @NonNull Reminder newItem) {
            return oldItem.Id == newItem.Id;
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Reminder oldItem, @NonNull Reminder newItem) {
            return oldItem == newItem;
        }
    }

    private class ReminderViewHolder extends RecyclerView.ViewHolder
    {
        private RemindersListItemBinding binding_;

        public ReminderViewHolder(RemindersListItemBinding binding)
        {
            super(binding.getRoot());
            binding_ = binding;
        }

        public void Bind(Reminder reminder, String header)
        {
            if (header != null)
            {
                binding_.setHeaderText(header);
                binding_.headerTextView.setVisibility(View.VISIBLE);
            }
            binding_.setReminder(reminder);
            binding_.executePendingBindings();
        }
    }

    private RemindersAdapter(@NonNull DiffUtil.ItemCallback<Reminder> diffCallback) {
        super(diffCallback);
    }

    public static RemindersAdapter Create()
    {
        return new RemindersAdapter(new ReminderDiffCallback());
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReminderViewHolder(
            RemindersListItemBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String GenerateHeader(int position)
    {
        if (position == 0 ||
                getItem(position).Time.getDayOfYear() != getItem(position - 1).Time.getDayOfYear())
        {
            LocalDateTime time = getItem(position).Time;
            LocalDateTime currentTime = LocalDateTime.now();
            if (time.getDayOfYear() == currentTime.getDayOfYear())
            {
                return "Today";
            }
            else if (time.getDayOfYear() == currentTime.getDayOfYear() + 1)
            {
                return "Tomorrow";
            }
            else
            {
                return time.format(DateTimeFormatter.ofPattern("LLLL dd, yyyy"));
            }
        }
        else
        {
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Reminder reminder = getItem(position);

        String header = GenerateHeader(position);

        ((ReminderViewHolder) holder).Bind(reminder, header);
    }
}
