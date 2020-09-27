package rs.ac.bg.etf.remindr.repositories;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import rs.ac.bg.etf.remindr.common.PersistenceRequest;
import rs.ac.bg.etf.remindr.database.Database;
import rs.ac.bg.etf.remindr.database.ReminderDao;
import rs.ac.bg.etf.remindr.models.Reminder;

public class ReminderRepository {

    private ReminderDao ReminderDao;
    private LiveData<List<Reminder>> allReminders_;
    private MutableLiveData<PersistenceRequest> updateRequest_;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ReminderRepository(Application application)
    {
        Database database = Database.GetDatabase(application);
        ReminderDao = database.ReminderDao();
        allReminders_ = ReminderDao.GetAllRemindersLaterThan(LocalDateTime.now());
        updateRequest_ = new MutableLiveData<>();
    }

    public LiveData<List<Reminder>> GetAllActiveReminders()
    {
        return allReminders_;
    }
    public MutableLiveData<PersistenceRequest> GetRequestStatus() { return updateRequest_; }

    public void InsertReminder(Reminder reminder)
    {
        Database.DatabaseWriterExecutor.execute(() -> {
            if (reminder.Description == null || reminder.Description.isEmpty() )
            {
                reminder.Description = "No Description";
            }
            ReminderDao.Insert(reminder);

            updateRequest_.postValue(new PersistenceRequest());
        });
    }

    public void DeleteReminder(Reminder reminder)
    {
        Database.DatabaseWriterExecutor.execute(() -> {
            ReminderDao.Delete(reminder);

            updateRequest_.postValue(new PersistenceRequest());
        });
    }

}
