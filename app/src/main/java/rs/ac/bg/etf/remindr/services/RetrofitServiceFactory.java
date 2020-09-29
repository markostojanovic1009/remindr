package rs.ac.bg.etf.remindr.services;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rs.ac.bg.etf.remindr.database.Converters;

public class RetrofitServiceFactory
{
    private static WebService instance_;

    private static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void write(JsonWriter out, LocalDateTime value) throws IOException {
            out.value(Converters.dateToTimestamp(value));
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public LocalDateTime read(JsonReader in) throws IOException {
            return Converters.fromTimestamp(in.nextLong());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static WebService GetWebService()
    {
        if (instance_ == null)
        {
            synchronized (RetrofitServiceFactory.class)
            {

                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                        .create();

                GsonConverterFactory factory = GsonConverterFactory.create(gson);
                instance_ = (new Retrofit.Builder())
                        .baseUrl("http://10.0.2.2:3000")
                        .addConverterFactory(factory)
                        .build()
                        .create(WebService.class);
            }
        }
        return instance_;
    }
}
