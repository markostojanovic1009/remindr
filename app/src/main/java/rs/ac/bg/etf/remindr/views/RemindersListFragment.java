package rs.ac.bg.etf.remindr.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.NavHostController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rs.ac.bg.etf.remindr.adapters.RemindersAdapter;
import rs.ac.bg.etf.remindr.databinding.RemindersListFragmentBinding;
import rs.ac.bg.etf.remindr.viewmodels.RemindersListViewModel;

public class RemindersListFragment extends Fragment {

    private RemindersListViewModel viewModel_;

    public static RemindersListFragment newInstance() {
        return new RemindersListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        RemindersListFragmentBinding binding = RemindersListFragmentBinding.inflate(inflater, container, false);
        viewModel_ = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())
                .create(RemindersListViewModel.class);

        RemindersAdapter adapter = RemindersAdapter.Create();
        binding.reminderList.setAdapter(adapter);
        binding.setClickListener(v -> NavigateToDetails());
        SubscribeUI(adapter);

        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void SubscribeUI(RemindersAdapter adapter)
    {
        viewModel_.GetReminders().observe(getViewLifecycleOwner(), adapter::submitList);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void NavigateToDetails()
    {
        NavDirections directions = RemindersListFragmentDirections.actionRemindersListToNewReminderFragment();
        NavController controller = NavHostFragment.findNavController(this);
        controller.navigate(directions);
    }

}