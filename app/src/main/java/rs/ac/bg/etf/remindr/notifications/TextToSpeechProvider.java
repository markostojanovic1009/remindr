package rs.ac.bg.etf.remindr.notifications;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class TextToSpeechProvider {

    private static TextToSpeech instance_;

    public static void Init(Context context)
    {
        instance_ = new TextToSpeech(
                context,
                status -> {
                    GetInstance(context).setLanguage(Locale.ENGLISH);
                });
    }

    public static TextToSpeech GetInstance(Context context)
    {
        return instance_;
    }

}
