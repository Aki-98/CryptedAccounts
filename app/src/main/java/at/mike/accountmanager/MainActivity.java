package at.mike.accountmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.security.crypto.EncryptedFile;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.Key;

public class MainActivity extends AppCompatActivity implements ActivityCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragment_main, new StartFragment()).commit();
    }

    @Override
    public void onCallback(String master_key, int mode) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = null;

        switch (mode) {
            case Constants.OPEN_ACC_FRG:
                fragment = new AccountFragment(master_key);
                break;
            case Constants.OPEN_ADD_ACC_FRG:
                fragment = new AddOrUpdAccountFragment(master_key, Constants.ADD_NEW_ACC);
            default:
                break;
        }

        if (fragment != null) {
            fm.beginTransaction().replace(R.id.fragment_main, fragment).commit();
        }
    }
}