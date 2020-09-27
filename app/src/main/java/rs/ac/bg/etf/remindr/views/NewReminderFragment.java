package rs.ac.bg.etf.remindr.views;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aigestudio.wheelpicker.WheelPicker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import rs.ac.bg.etf.remindr.R;
import rs.ac.bg.etf.remindr.database.Converters;
import rs.ac.bg.etf.remindr.databinding.NewReminderFragmentBinding;
import rs.ac.bg.etf.remindr.models.Reminder;
import rs.ac.bg.etf.remindr.notifications.NotificationReceiver;
import rs.ac.bg.etf.remindr.viewmodels.NewReminderViewModel;

public class NewReminderFragment extends Fragment {

    public static final String DEFAULT_NOTIFICATION_CHANNEL_ID = "DEFAULT_NOTIFICATION_CHANNEL_ID";
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private NewReminderViewModel viewModel_;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        NewReminderFragmentBinding binding = NewReminderFragmentBinding.inflate(inflater, container, false);
        viewModel_ = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())
                .create(NewReminderViewModel.class);

        binding.setViewModel(viewModel_);
        binding.setLifecycleOwner(this);

        binding.dateWheelPicker.setData(CreateDateListForPicker());
        binding.dateWheelPicker.setOnItemSelectedListener((WheelPicker picker, Object data, int position) -> viewModel_.SetSelectedDate(LocalDate.now().plusDays(position)));

        binding.hourWheelPicker.setData(CreateHourListForPicker());
        binding.hourWheelPicker.setOnItemSelectedListener((WheelPicker picker, Object data, int position) -> viewModel_.SetSelectedHour(position));
        int hour = LocalDateTime.now().getHour();
        binding.hourWheelPicker.setSelectedItemPosition(hour);

        binding.minuteWheelPicker.setData(CreateMinuteListForPicker());
        binding.minuteWheelPicker.setOnItemSelectedListener((WheelPicker picker, Object data, int position) -> viewModel_.SetSelectedMinute(position));
        binding.minuteWheelPicker.setSelectedItemPosition(LocalDateTime.now().getMinute());

        binding.button.setOnClickListener((view) ->
        {
            String error = viewModel_.ValidateReminder();
            if (error.isEmpty())
            {
                viewModel_.CreateReminder();
                binding.createErrorText.setVisibility(View.GONE);
            }
            else
            {
                binding.createErrorText.setText(error);
                binding.createErrorText.setVisibility(View.VISIBLE);
            }
        });

        viewModel_.GetRequestStatus().observe(getViewLifecycleOwner(), persistenceRequest -> {
            if (persistenceRequest.IsSuccess)
            {
                ScheduleNotification();
                NavController controller = NavHostFragment.findNavController(this);
                controller.popBackStack();
            }
        });

        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void ScheduleNotification()
    {
        Notification notification = CreateReminderNotification();

        Intent notificationIntent = new Intent(getContext(), NotificationReceiver.class);
        notificationIntent.putExtra(NotificationReceiver.NOTIFICATION_ID , 1 );
        notificationIntent.putExtra(NotificationReceiver.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        long notificationTime = Converters.dateToTimestamp(viewModel_.GetReminderData().getValue().Time);
        Log.w("DATECONVERSIONTAG", "" + notificationTime);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent);
    }

    private Notification CreateReminderNotification()
    {
        Reminder reminder = viewModel_.GetReminderData().getValue();

        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);

        return new NotificationCompat.Builder(getContext(), DEFAULT_NOTIFICATION_CHANNEL_ID)
                .setContentTitle(reminder.Title)
                .setContentText(reminder.Description)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true)
                .setChannelId(NOTIFICATION_CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .build();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<LocalDate> CreateDateListForPicker()
    {
        List<LocalDate> list = new ArrayList<>();
        LocalDate date = LocalDate.now();

        for(int i = 0; i < 365; i++)
        {
            list.add(date);
            date = date.plusDays(1);
        }

        return list;
    }

    private List<String> CreateHourListForPicker()
    {
        List<String> list = new ArrayList<>();
        for (Integer i = 0; i < 24; i++)
        {
            list.add(i < 10 ? "0" + i.toString() : i.toString());
        }
        return list;
    }

    private List<String> CreateMinuteListForPicker()
    {
        List<String> list = new ArrayList<>();
        for (Integer i = 0; i < 60; i++)
        {
            list.add(i < 10 ? "0" + i.toString() : i.toString());
        }
        return list;
    }

}