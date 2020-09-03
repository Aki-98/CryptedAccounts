package at.mike.accountmanager;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class AccountFragment extends Fragment {

    private ActivityCallback activityCallbackListener;

    public AccountFragment(String master_key) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_accounts);
        AccountAdapter accountAdapter = new AccountAdapter();

        recyclerView.setAdapter(accountAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

        AccountViewModel accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        accountViewModel.getAllAccounts().observe(getViewLifecycleOwner(), new Observer<List<Account>>() {
            @Override
            public void onChanged(List<Account> accounts) {
                accountAdapter.setAccounts(accounts);
            }
        });

        FloatingActionButton fabAddAcc = view.findViewById(R.id.fab_add_account);
        fabAddAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityCallbackListener.onCallback(null,Constants.ADD_NEW_ACC);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof ActivityCallback) {
            activityCallbackListener = (ActivityCallback) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activityCallbackListener = null;
    }
}
