package at.mike.accountmanager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.util.List;

import javax.crypto.AEADBadTagException;

public class AddOrUpdAccountFragment extends Fragment {

    private final String TAG = "AddOrUpdAccountFragment";
    private TextInputEditText textPlatform;
    private TextInputEditText textMail;
    private TextInputEditText textUsername;
    private TextInputEditText textPassword;
    private ImageView imageLogo;
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
        imageLogo = view.findViewById(R.id.imageViewLogo);

        encryptionManager = new EncryptionManager(view.getContext());

        if (account != null) {
            initializeViews();
        }
        else {
            account = new Account();
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

        imageLogo.setOnClickListener(l -> {
            ImagePicker.create(this)
                    .single()
                    .limit(1)
                    .start();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image image = ImagePicker.getFirstImageOrNull(data);
            Uri imageUri = image.getUri();

            // load image in ImageView
            loadLogo(imageUri);

            account.setLogo(imageUri.toString());
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void saveAccount() {
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

    private void initializeViews() {
        textPlatform.setText(account.getPlatform());
        textMail.setText(account.getEmail());
        textUsername.setText(account.getUsername());
        textPassword.setText(account.getPassword());

        if (account.getLogo() != null) {
            loadLogo(Uri.parse(account.getLogo()));
        }
    }

    private void loadLogo(Uri logo) {
        Log.d(TAG, "logo path: " + logo);

        GlideApp.with(this)
                .load(logo)
                .into(imageLogo);
    }
}
