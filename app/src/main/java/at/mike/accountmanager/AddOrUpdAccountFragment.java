package at.mike.accountmanager;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import javax.crypto.AEADBadTagException;

public class AddOrUpdAccountFragment extends Fragment {

    private final String TAG = "AddOrUpdAccountFragment";
    private TextInputEditText textPlatform;
    private TextInputEditText textMail;
    private TextInputEditText textUsername;
    private TextInputEditText textPassword;
    private String masterKey;
    private ActivityCallback activityCallbackListener;
    private Toolbar toolbar;
    private Account account;
    private EncryptionManager encryptionManager;

    public AddOrUpdAccountFragment(String master_key, int mode, Account account) {
        this.masterKey = master_key;
        this.account = account;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_or_upd_account, container, false);
        setHasOptionsMenu(true);

        textPlatform = view.findViewById(R.id.textPlatform);
        textMail = view.findViewById(R.id.textMail);
        textUsername = view.findViewById(R.id.textUsername);
        textPassword = view.findViewById(R.id.textPassword);
        toolbar = view.findViewById(R.id.toolbar);

        encryptionManager = new EncryptionManager(view.getContext());

        if (account != null) {
            initializeTextViews();
        }

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.account_update) {
                if (isDataValid()) {
                    saveAccount();
                    activityCallbackListener.onCallback(masterKey, Constants.OPEN_ACC_FRG, null);
                }
            }
            else if (item.getItemId() == R.id.account_delete) {
                if (textPlatform.getText().length() > 0) {
                    try {
                        encryptionManager.deleteAccount(masterKey, textPlatform.getText().toString());
                        activityCallbackListener.onCallback(masterKey, Constants.OPEN_ACC_FRG, null);
                    } catch (AEADBadTagException e) {
                        e.printStackTrace();
                    }
                }
            }

            return true;
        });

        return view;
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

    private void saveAccount() {
        Account account = new Account();

        account.setPlatform(textPlatform.getText().toString());
        account.setEmail(textMail.getText().toString());
        account.setUsername(textUsername.getText().toString());
        account.setPassword(textPassword.getText().toString());

        EncryptionManager encryptionManager = new EncryptionManager(getContext());

        try {
            encryptionManager.writeOrUpdateAccount(account, masterKey);
        } catch (AEADBadTagException e) {
            e.printStackTrace();
        }
    }

    private boolean isDataValid() {
        if (textPlatform.getText().length() < 1) {
            textPlatform.setError("Platform is empty");
            return false;
        }
        else if (textMail.getText().length() < 1) {
            textMail.setError("Mail is empty");
            return false;
        }
        else if (textPassword.getText().length() < 1) {
            textPassword.setError("Password is empty");
            return false;
        }

        return true;
    }

    private void initializeTextViews() {
        textPlatform.setText(account.getPlatform());
        textMail.setText(account.getEmail());
        textUsername.setText(account.getUsername());
        textPassword.setText(account.getPassword());
    }
}
