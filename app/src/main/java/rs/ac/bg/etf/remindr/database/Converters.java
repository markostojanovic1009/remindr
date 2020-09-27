package rs.ac.bg.etf.remindr.database;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;

public class Converters {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public static LocalDateTime fromTimestamp(Long value)
    {
        return value == null ?
                null : LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.of("CET"));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public static Long dateToTimestamp(LocalDateTime date)
    {
        return date == null ? null : date.toInstant(ZoneOffset.ofHours(2)).toEpochMilli();
    }
}
