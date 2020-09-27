package rs.ac.bg.etf.remindr.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

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

        public void Bind(Reminder reminder)
        {
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

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Reminder reminder = getItem(position);
        ((ReminderViewHolder) holder).Bind(reminder);
    }
}
