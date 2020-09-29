package rs.ac.bg.etf.remindr.viewmodels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;

import java.util.List;

import rs.ac.bg.etf.remindr.common.Constants;
import rs.ac.bg.etf.remindr.models.Reminder;
import rs.ac.bg.etf.remindr.repositories.ReminderRepository;

public class RemindersListViewModel extends AndroidViewModel {

    private final ReminderRepository reminderRepository_;
    private final SavedStateHandle savedStateHandle_;
    private String token_;
    private LiveData<List<Reminder>> reminders_;
    private SharedPreferences preferences_;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public RemindersListViewModel(@NonNull Application application, SavedStateHandle savedStateHandle) {
        super(application);
        reminderRepository_ = new ReminderRepository(application);
        savedStateHandle_ = savedStateHandle;
        preferences_ = application.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        token_ = preferences_.getString(Constants.AUTHORIZATION_TOKEN_KEY, "");
    }

    public LiveData<List<Reminder>> GetReminders()
    {
        if (reminders_ == null)
        {
            reminders_ = reminderRepository_.GetAllActiveReminders(token_);
        }
        return reminders_;
    }

    public void RemoveReminder(int position)
    {
        Reminder reminder = reminders_.getValue().get(position);
        reminderRepository_.DeleteReminder(reminder, token_);
    }

    public boolean IsUserLoggedIn() {
        token_ = preferences_.getString(Constants.AUTHORIZATION_TOKEN_KEY, "");
        return !token_.isEmpty();
    }

    public SavedStateHandle GetSavedStateHandle() {
        return savedStateHandle_;
    }
}