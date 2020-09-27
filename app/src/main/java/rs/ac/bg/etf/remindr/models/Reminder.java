package rs.ac.bg.etf.remindr.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "reminders")
public class Reminder {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public long Id;

    public String Title;
    public String Description;
    public Date Time;

}
