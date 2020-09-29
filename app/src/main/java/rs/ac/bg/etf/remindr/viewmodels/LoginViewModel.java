package rs.ac.bg.etf.remindr.viewmodels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import rs.ac.bg.etf.remindr.common.Constants;
import rs.ac.bg.etf.remindr.models.JWToken;
import rs.ac.bg.etf.remindr.models.User;
import rs.ac.bg.etf.remindr.repositories.UserRepository;

public class LoginViewModel extends AndroidViewModel {

    private final SavedStateHandle savedStateHandle_;
    private final UserRepository userRepository_;
    private final SharedPreferences preferences_;
    private LiveData<JWToken> token_;
    private MutableLiveData<User> user_;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LoginViewModel(Application application, SavedStateHandle savedStateHandle)
    {
        super(application);
        savedStateHandle_ = savedStateHandle;
        userRepository_ = new UserRepository();
        token_ = userRepository_.GetJWTokenData();
        user_ = new MutableLiveData<>(new User());
        preferences_ = application.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public MutableLiveData<User> GetUser() { return user_; }
    public LiveData<JWToken> GetToken() { return token_; }

    public void Login()
    {
        userRepository_.Login(user_.getValue());
    }

    public void SignUp() {
        userRepository_.SignUp(user_.getValue());
    }

    public void SaveToken(String token) {
        SharedPreferences.Editor editor = preferences_.edit();
        editor.putString(Constants.AUTHORIZATION_TOKEN_KEY, token);
        editor.commit();
    }
}