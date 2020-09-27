package rs.ac.bg.etf.remindr.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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

    public ReminderRepository(Application application)
    {
        Database database = Database.GetDatabase(application);
        ReminderDao = database.ReminderDao();
        allReminders_ = ReminderDao.GetAllRemindersLaterThan(new Date());
        updateRequest_ = new MutableLiveData<>();
    }

    public LiveData<List<Reminder>> GetAllActiveReminders()
    {
        return allReminders_;
    }
    public MutableLiveData<PersistenceRequest> GetRequestStatus() { return updateRequest_; }

    public void InsertReminder(final Reminder reminder)
    {
        Database.DatabaseWriterExecutor.execute(() -> {
            ReminderDao.Insert(reminder);

            updateRequest_.postValue(new PersistenceRequest());
        });
    }

}
