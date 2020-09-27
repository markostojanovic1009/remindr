package rs.ac.bg.etf.remindr.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rs.ac.bg.etf.remindr.models.Reminder;

@androidx.room.Database(entities = {Reminder.class}, version = 1, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class Database extends RoomDatabase {

    public abstract ReminderDao ReminderDao();

    private static volatile Database Instance;

    private static final int NumberOfThreads = 4;

    public static final ExecutorService DatabaseWriterExecutor = Executors.newFixedThreadPool(NumberOfThreads);

    public static Database GetDatabase(final Context context)
    {
        if (Instance == null)
        {
            synchronized (Database.class)
            {
                if (Instance == null)
                {
                    Instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            Database.class,
                            "database")
                            .build();
                }
            }
        }
        return Instance;
    }
}
