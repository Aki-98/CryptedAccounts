package at.mike.accountmanager;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import javax.crypto.AEADBadTagException;

public class AccountFragment extends Fragment {

    private final String TAG = "AccountFragment";
    private ActivityCallback activityCallbackListener;
    private String masterKey;

    public AccountFragment(String master_key) {
        masterKey = master_key;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_accounts);
        EncryptionManager encryptionManager = new EncryptionManager(getContext());
        AccountAdapter accountAdapter = new AccountAdapter();

        try {
            accountAdapter.setAccounts(encryptionManager.getEncryptedAccounts(masterKey));
        } catch (AEADBadTagException e) {
            e.printStackTrace();
        }

        recyclerView.setAdapter(accountAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

        FloatingActionButton fabAddAcc = view.findViewById(R.id.fab_add_account);
        fabAddAcc.setOnClickListener((View v) -> {
            activityCallbackListener.onCallback(masterKey, Constants.ADD_NEW_ACC);
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
