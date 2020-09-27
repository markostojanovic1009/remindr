package rs.ac.bg.etf.remindr.models;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(tableName = "reminders")
public class Reminder {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public long Id = 0;

    public String Title;

    public String Description;

    public LocalDateTime Time;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String Validate()
    {
        if (Title == null || Title.isEmpty())
        {
            return "Title cannot be empty";
        }

        if (Time.compareTo(LocalDateTime.now()) < 0)
        {
            return "Time cannot be in the past";
        }

        return "";
    }

}
