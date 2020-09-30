package rs.ac.bg.etf.remindr.repositories;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rs.ac.bg.etf.remindr.models.JWToken;
import rs.ac.bg.etf.remindr.models.User;
import rs.ac.bg.etf.remindr.services.RetrofitServiceFactory;
import rs.ac.bg.etf.remindr.services.WebService;

public class UserRepository
{
    private WebService service_;

    private MutableLiveData<JWToken> jwTokenData_;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public UserRepository()
    {
        service_ = RetrofitServiceFactory.GetWebService();
        jwTokenData_ = new MutableLiveData<>();
    }

    public void Login(User user)
    {
        service_.Login(user).enqueue(new Callback<JWToken>() {
            @Override
            public void onResponse(Call<JWToken> call, Response<JWToken> response) {
                jwTokenData_.setValue(response.body());
            }

            @Override
            public void onFailure(Call<JWToken> call, Throwable t) {

            }
        });
    }

    public void SignUp(User user)
    {
        service_.SignUp(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                JWToken token = new JWToken();
                token.Token = response.body().Token;
                jwTokenData_.setValue(token);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });
    }

    public MutableLiveData<JWToken> GetJWTokenData() {
        return jwTokenData_;
    }
}
