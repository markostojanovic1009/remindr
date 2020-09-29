package rs.ac.bg.etf.remindr.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rs.ac.bg.etf.remindr.common.Constants;
import rs.ac.bg.etf.remindr.databinding.LoginFragmentBinding;
import rs.ac.bg.etf.remindr.viewmodels.LoginViewModel;

public class LoginFragment extends Fragment {

    private LoginViewModel viewModel_;
    private SavedStateHandle savedStateHandle_;
    private LoginFragmentBinding binding_;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewModel_ = new ViewModelProvider(
                this,
                new SavedStateViewModelFactory(getActivity().getApplication(),this))
                .get(LoginViewModel.class);

        binding_ = LoginFragmentBinding.inflate(inflater, container, false);
        binding_.setViewModel(viewModel_);
        binding_.setLifecycleOwner(getViewLifecycleOwner());
        binding_.loginButton.setOnClickListener((v) -> Login(false));
        binding_.signUpButton.setOnClickListener((v) -> Login(true));

        viewModel_.GetToken().observe(getViewLifecycleOwner(), jwToken -> {
            if (jwToken != null)
            {
                viewModel_.SaveToken("Bearer " + jwToken.Token);
                savedStateHandle_.set(Constants.LOGIN_SUCCESSFUL, true);
                NavHostFragment.findNavController(this).popBackStack();
            }
        });

        savedStateHandle_ = NavHostFragment.findNavController(this)
                .getPreviousBackStackEntry()
                .getSavedStateHandle();
        savedStateHandle_.set(Constants.LOGIN_SUCCESSFUL, false);

        return binding_.getRoot();
    }

    private void Login(boolean isSignup) {
        String error = viewModel_.GetUser().getValue().Validate();

        if (error.isEmpty())
        {
            if (isSignup)
            {
                viewModel_.SignUp();
            }
            else
            {
                viewModel_.Login();
            }
            binding_.errorText.setVisibility(View.INVISIBLE);
        }
        else
        {
            binding_.errorText.setText(error);
            binding_.errorText.setVisibility(View.VISIBLE);
        }
    }
}