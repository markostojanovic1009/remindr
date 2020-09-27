package rs.ac.bg.etf.remindr.views;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aigestudio.wheelpicker.WheelPicker;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import rs.ac.bg.etf.remindr.databinding.NewReminderFragmentBinding;
import rs.ac.bg.etf.remindr.viewmodels.NewReminderViewModel;

public class NewReminderFragment extends Fragment {

    private NewReminderViewModel viewModel_;

    public static NewReminderFragment newInstance() {
        return new NewReminderFragment();
    }

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

        binding.minuteWheelPicker.setData(CreateMinuteListForPicker());
        binding.minuteWheelPicker.setOnItemSelectedListener((WheelPicker picker, Object data, int position) -> viewModel_.SetSelectedMinute(position));

        binding.button.setOnClickListener((view) -> viewModel_.CreateReminder());

        viewModel_.GetRequestStatus().observe(getViewLifecycleOwner(), persistenceRequest -> {
            if (persistenceRequest.IsSuccess)
            {
                NavController controller = NavHostFragment.findNavController(this);
                controller.popBackStack();
            }
        });

        return binding.getRoot();
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