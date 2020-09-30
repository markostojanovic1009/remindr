package rs.ac.bg.etf.remindr.services;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rs.ac.bg.etf.remindr.models.JWToken;
import rs.ac.bg.etf.remindr.models.Reminder;
import rs.ac.bg.etf.remindr.models.User;

public interface WebService {

    // User API routes
    @POST("/api/users/signup")
    Call<User> SignUp(@Body User user);

    @POST("/api/users/login")
    Call<JWToken> Login(@Body User user);


    // Reminders API routes
    @POST("/api/reminders/create")
    Call<Reminder> CreateReminder(@Header("Authorization") String tokenString, @Body Reminder reminder);

    @GET("/api/reminders")
    Call<List<Reminder>> GetRemindersLaterThan(
            @Header("Authorization") String token,
            @Query("user") String userEmail,
            @Query("after") long timestamp);

    @DELETE("/api/reminders/{id}")
    Call<ResponseBody> DeleteReminder(
            @Header("Authorization") String tokenString,
            @Path("id") long reminderId);
}
