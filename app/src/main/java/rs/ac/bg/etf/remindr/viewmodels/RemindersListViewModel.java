package rs.ac.bg.etf.remindr.viewmodels;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import rs.ac.bg.etf.remindr.models.Reminder;
import rs.ac.bg.etf.remindr.repositories.ReminderRepository;

public class RemindersListViewModel extends AndroidViewModel {

    private final ReminderRepository reminderRepository_;
    private LiveData<List<Reminder>> reminders_;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public RemindersListViewModel(@NonNull Application application) {
        super(application);
        reminderRepository_ = new ReminderRepository(application);
        reminders_ = reminderRepository_.GetAllActiveReminders();
    }

    public LiveData<List<Reminder>> GetReminders()
    {
        return reminders_;
    }

    public void RemoveReminder(int position)
    {
        Reminder reminder = reminders_.getValue().get(position);
        reminderRepository_.DeleteReminder(reminder);
    }
}