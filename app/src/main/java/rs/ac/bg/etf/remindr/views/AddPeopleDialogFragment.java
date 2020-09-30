package rs.ac.bg.etf.remindr.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;
import java.util.Arrays;

import rs.ac.bg.etf.remindr.R;
import rs.ac.bg.etf.remindr.databinding.AddPeopleDialogFragmentBinding;
import rs.ac.bg.etf.remindr.viewmodels.NewReminderViewModel;

public class AddPeopleDialogFragment extends DialogFragment {

    private ArrayList<String> emails_;
    private ArrayAdapter<String> adapter_;
    public ObservableField<String> NewEmail;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        emails_ = new ArrayList<>(
                Arrays.asList(
                        AddPeopleDialogFragmentArgs.fromBundle(getArguments()).getEmailList()));

        AddPeopleDialogFragmentBinding binding =
                AddPeopleDialogFragmentBinding.inflate(inflater, container, false);

        NewReminderViewModel viewModel = new ViewModelProvider(
                requireActivity(),
                new SavedStateViewModelFactory(getActivity().getApplication(),this))
                .get(NewReminderViewModel.class);
        NewEmail = new ObservableField<>("");

        adapter_ = new ArrayAdapter<>(getContext(), R.layout.email_list_item);
        adapter_.addAll(emails_);
        binding.emailList.setAdapter(adapter_);

        binding.addEmailButton.setOnClickListener((v) -> {
            emails_.add(NewEmail.get());
            adapter_.add(NewEmail.get());
            adapter_.notifyDataSetChanged();
        });

        binding.finishAddButton.setOnClickListener(v -> {
            viewModel.AddPeopleToReminder(emails_);
            NavHostFragment.findNavController(this).popBackStack();
        });

        binding.setFragment(this);

        return binding.getRoot();
    }
}
