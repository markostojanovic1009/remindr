package rs.ac.bg.etf.remindr.notifications;

import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class VoiceNotificationListenerService extends NotificationListenerService {
    private TextToSpeech textToSpeech_;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Log.w("TTS", "onNotificationPosted called with package name: " + sbn.getPackageName());

        if (sbn.getPackageName().equals("rs.ac.bg.etf.remindr"))
        {
            String content = "New reminder " + sbn.getNotification().extras.getString("android.title");
            Log.w("TTS", "onNotificationPosted called with content: " + content);

            TextToSpeechProvider.GetInstance(getApplicationContext()).speak(content, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

}
