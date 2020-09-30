package rs.ac.bg.etf.remindr.repositories;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rs.ac.bg.etf.remindr.common.Constants;
import rs.ac.bg.etf.remindr.common.PersistenceRequest;
import rs.ac.bg.etf.remindr.database.Database;
import rs.ac.bg.etf.remindr.database.ReminderDao;
import rs.ac.bg.etf.remindr.models.Reminder;
import rs.ac.bg.etf.remindr.services.RetrofitServiceFactory;
import rs.ac.bg.etf.remindr.services.WebService;

public class ReminderRepository {

    private ReminderDao ReminderDao;
    private LiveData<List<Reminder>> allReminders_;
    private MutableLiveData<PersistenceRequest> updateRequest_;
    private WebService webService_;
    private long cachedAt_;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ReminderRepository(Application application)
    {
        Database database = Database.GetDatabase(application);
        ReminderDao = database.ReminderDao();
        allReminders_ = ReminderDao.GetAllRemindersLaterThan(LocalDateTime.now());
        updateRequest_ = new MutableLiveData<>();
        webService_ = RetrofitServiceFactory.GetWebService();
        cachedAt_ = 0;
    }

    public LiveData<List<Reminder>> GetAllActiveReminders(String authorizationToken, String userEmail)
    {
        long currentTime = System.currentTimeMillis();
        if (currentTime > cachedAt_ + Constants.CACHE_TIMEOUT_IN_MILIS)
        {
            cachedAt_ = currentTime;
            webService_.GetRemindersLaterThan(authorizationToken, userEmail, currentTime)
                    .enqueue(new Callback<List<Reminder>>() {
                @Override
                public void onResponse(Call<List<Reminder>> call, Response<List<Reminder>> response) {
                    Database.DatabaseWriterExecutor.execute(() -> {
                        ReminderDao.InsertAll(response.body());
                    });
                }

                @Override
                public void onFailure(Call<List<Reminder>> call, Throwable t) {

                }
            });
        }
        return allReminders_;
    }

    public MutableLiveData<PersistenceRequest> GetRequestStatus() { return updateRequest_; }

    public void InsertReminder(Reminder reminder, String authorizationToken)
    {
        if (reminder.Description == null || reminder.Description.isEmpty() )
        {
            reminder.Description = "No Description";
        }

        webService_.CreateReminder(authorizationToken, reminder).enqueue(new Callback<Reminder>() {
            @Override
            public void onResponse(Call<Reminder> call, Response<Reminder> response) {
                Database.DatabaseWriterExecutor.execute(() -> {
                    ReminderDao.Insert(response.body());
                    updateRequest_.postValue(new PersistenceRequest());
                });
            }

            @Override
            public void onFailure(Call<Reminder> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void DeleteReminder(Reminder reminder, String token)
    {
        webService_.DeleteReminder(token, reminder.Id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Database.DatabaseWriterExecutor.execute(() -> {
                    ReminderDao.Delete(reminder);
                    updateRequest_.postValue(new PersistenceRequest());
                });
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

}
