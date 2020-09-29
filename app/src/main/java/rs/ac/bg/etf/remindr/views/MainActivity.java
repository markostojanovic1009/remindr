package rs.ac.bg.etf.remindr.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import rs.ac.bg.etf.remindr.R;
import rs.ac.bg.etf.remindr.notifications.TextToSpeechProvider;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextToSpeechProvider.Init(this);
    }
}