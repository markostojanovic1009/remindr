package rs.ac.bg.etf.remindr.viewmodels;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


import java.time.LocalDate;
import java.time.LocalDateTime;

import rs.ac.bg.etf.remindr.models.Reminder;
import rs.ac.bg.etf.remindr.common.PersistenceRequest;
import rs.ac.bg.etf.remindr.repositories.ReminderRepository;

public class NewReminderViewModel extends AndroidViewModel {

    private MutableLiveData<Reminder> reminderData_;
    private Reminder reminder_;
    private MutableLiveData<PersistenceRequest> createRequest_;

    private final ReminderRepository reminderRepository_;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public NewReminderViewModel(@NonNull Application application)
    {
        super(application);
        reminderRepository_ = new ReminderRepository(application);
        reminder_ = new Reminder();
        reminder_.Time = LocalDateTime.now().withHour(0).withMinute(0);
        reminderData_ = new MutableLiveData<>(reminder_);
        createRequest_ = reminderRepository_.GetRequestStatus();
    }

    public MutableLiveData<Reminder> GetReminderData()
    {
        return reminderData_;
    }

    public void CreateReminder()
    {
        reminderRepository_.InsertReminder(reminder_);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String ValidateReminder()
    {
        return reminder_.Validate();
    }

    public MutableLiveData<PersistenceRequest> GetRequestStatus()
    {
        return createRequest_;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void SetSelectedDate(LocalDate date)
    {
        /*reminder_.Time = LocalDateTime.of(
                date,
                date.atTime(
                    reminder_.Time.getHour(),
                    reminder_.Time.getMinute()).toLocalTime());
         */
        reminder_.Time = reminder_.Time.withDayOfYear(date.getDayOfYear());
        reminderData_.setValue(reminder_);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void SetSelectedHour(int hours)
    {
        reminder_.Time = reminder_.Time.withHour(hours);
        reminderData_.setValue(reminder_);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void SetSelectedMinute(int minutes) {
        reminder_.Time = reminder_.Time.withHour(minutes);
        reminderData_.setValue(reminder_);
    }
}