package rs.ac.bg.etf.remindr.viewmodels;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import rs.ac.bg.etf.remindr.models.Reminder;
import rs.ac.bg.etf.remindr.common.PersistenceRequest;
import rs.ac.bg.etf.remindr.repositories.ReminderRepository;

public class NewReminderViewModel extends AndroidViewModel {

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static class DateWrapper
    {
        public LocalDate LocalDate = java.time.LocalDate.now();
        public int Hour = 0;
        public int Minute = 0;

        @RequiresApi(api = Build.VERSION_CODES.O)
        public Date ToDate()
        {
            Date current = Date.from(LocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            Calendar c = Calendar.getInstance();
            c.setTime(current);
            c.add(Calendar.HOUR_OF_DAY, Hour);
            c.add(Calendar.MINUTE, Minute);

            return c.getTime();
        }
    }

    private MutableLiveData<Reminder> reminderData_;
    private Reminder reminder_;
    private final DateWrapper dateWrapper_;
    private MutableLiveData<PersistenceRequest> createRequest_;

    private final ReminderRepository reminderRepository_;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public NewReminderViewModel(@NonNull Application application)
    {
        super(application);
        reminderRepository_ = new ReminderRepository(application);
        reminder_ = new Reminder();
        reminderData_ = new MutableLiveData<>(reminder_);
        dateWrapper_ = new DateWrapper();
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

    public MutableLiveData<PersistenceRequest> GetRequestStatus()
    {
        return createRequest_;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void SetSelectedDate(LocalDate date)
    {
        dateWrapper_.LocalDate = date;
        reminder_.Time = dateWrapper_.ToDate();
        reminderData_.setValue(reminder_);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void SetSelectedHour(int hours)
    {
        dateWrapper_.Hour = hours;
        reminder_.Time = dateWrapper_.ToDate();
        reminderData_.setValue(reminder_);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void SetSelectedMinute(int minutes) {
        dateWrapper_.Minute = minutes;
        reminder_.Time = dateWrapper_.ToDate();
        reminderData_.setValue(reminder_);
    }
}