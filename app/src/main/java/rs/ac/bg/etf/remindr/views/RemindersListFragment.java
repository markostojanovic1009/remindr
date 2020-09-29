package rs.ac.bg.etf.remindr.views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rs.ac.bg.etf.remindr.R;
import rs.ac.bg.etf.remindr.adapters.RemindersAdapter;
import rs.ac.bg.etf.remindr.common.Constants;
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
        viewModel_ = new ViewModelProvider(
         this,
                new SavedStateViewModelFactory(getActivity().getApplication(),this))
                .get(RemindersListViewModel.class);

        NavController controller = NavHostFragment.findNavController(this);
        if (!viewModel_.IsUserLoggedIn())
        {
            controller.navigate(R.id.loginFragment);
            return null;
        }

        RemindersAdapter adapter = RemindersAdapter.Create();
        binding.reminderList.setAdapter(adapter);
        binding.setClickListener(v -> NavigateToDetails());
        SubscribeUI(adapter);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(
                    RecyclerView recyclerView,
                    RecyclerView.ViewHolder viewHolder,
                    RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                int position = viewHolder.getAdapterPosition();
                viewModel_.RemoveReminder(position);
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onChildDraw(@NonNull Canvas c,
                                    @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX,
                                    float dY,
                                    int actionState,
                                    boolean isCurrentlyActive)
            {
                c.clipRect(0f, viewHolder.itemView.getTop(), dX, viewHolder.itemView.getBottom());
                c.drawColor(Color.GRAY);

                int textMargin = 10;

                Drawable trashBinIcon = getResources().getDrawable(R.drawable.ic_baseline_delete_24,null);
                trashBinIcon.setBounds(new Rect(
                        textMargin,
                        viewHolder.itemView.getTop() + textMargin,
                        textMargin + trashBinIcon.getIntrinsicWidth(),
                        viewHolder.itemView.getTop() + trashBinIcon.getIntrinsicHeight() + textMargin));
                trashBinIcon.draw(c);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(binding.reminderList);

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