package rs.ac.bg.etf.remindr.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

import rs.ac.bg.etf.remindr.models.Reminder;

@Dao
public interface ReminderDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void Insert(Reminder reminder);

    @Delete
    void Delete(Reminder reminder);

    @Query("SELECT * FROM reminders WHERE Time > :laterThanTime ORDER BY Time ASC")
    LiveData<List<Reminder>> GetAllRemindersLaterThan(Date laterThanTime);
}
