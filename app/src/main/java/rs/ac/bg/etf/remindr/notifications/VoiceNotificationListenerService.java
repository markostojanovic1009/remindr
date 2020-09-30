package rs.ac.bg.etf.remindr.notifications;

import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.speech.tts.TextToSpeech;

import androidx.annotation.RequiresApi;


@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class VoiceNotificationListenerService extends NotificationListenerService {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);

        if (sbn.getPackageName().equals("rs.ac.bg.etf.remindr"))
        {
            String content = "New reminder " + sbn.getNotification().extras.getString("android.title");
            TextToSpeechProvider.GetInstance(getApplicationContext())
                    .speak(
                            content,
                            TextToSpeech.QUEUE_FLUSH,
                            null);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

}
