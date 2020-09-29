package rs.ac.bg.etf.remindr.models;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

@Entity(tableName = "reminders")
public class Reminder {
    @PrimaryKey
    @NonNull
    @SerializedName(value = "id")
    public long Id;

    @SerializedName(value = "title")
    public String Title;

    @SerializedName(value = "description")
    public String Description;

    @SerializedName(value = "time")
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
