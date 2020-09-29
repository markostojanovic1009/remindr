package rs.ac.bg.etf.remindr.models;

import com.google.gson.annotations.SerializedName;

public class User
{
    @SerializedName(value = "id")
    public Long Id;

    @SerializedName(value = "email")
    public String Email;

    @SerializedName(value = "password")
    public String Password;

    // Abusing User object to get token as well.
    // It should be alongside User object in a wrapper object
    @SerializedName(value = "token")
    public String Token;

    public String Validate() {
        if (Email == null || Email.isEmpty())
        {
            return "E-mail field required";
        }

        if (Password == null || Password.length() < 3)
        {
            return "Password must be at least 3 characters";
        }

        return "";
    }
}
