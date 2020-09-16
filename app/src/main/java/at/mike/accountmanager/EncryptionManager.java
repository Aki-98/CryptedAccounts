package at.mike.accountmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.google.gson.Gson;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.crypto.AEADBadTagException;

public class EncryptionManager {

    private final String TAG = "EncryptionManager";
    private Context context;
    EncryptedSharedPreferences encryptedSharedPreferences;

    public EncryptionManager(Context context) {
        this.context = context;
    }

    public void createKeyStore(String master_key) throws AEADBadTagException {
        SharedPreferences.Editor editor = getEncryptedSharedPreferences(master_key).edit();
        editor.putString("MASTER_KEY", master_key);
        editor.apply();
    }

    public List<Account> readAll() {
        //SharedPreferences.Editor sharedPrefsEditor = encryptedSharedPreferences.edit();

        return null;
    }

    public Account readAccount(int account_id) {
        return null;
    }

    public void writeOrUpdateAccount(Account account, String master_key) throws AEADBadTagException {
        encryptedSharedPreferences = getEncryptedSharedPreferences(master_key);

        Gson gson = new Gson();
        SharedPreferences.Editor sharedPrefsEditor = encryptedSharedPreferences.edit();
        sharedPrefsEditor.putString(account.getPlatform(), gson.toJson(account));
        sharedPrefsEditor.apply();
    }

    public String get(String name, String master_key) throws AEADBadTagException {
        encryptedSharedPreferences = getEncryptedSharedPreferences(master_key);

        return encryptedSharedPreferences.getString(name, "default");
    }

    private EncryptedSharedPreferences getEncryptedSharedPreferences(String master_key) throws AEADBadTagException {
        try {
            KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder(
                    master_key,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setKeySize(256)
                    .build();

            MasterKey masterKey = new MasterKey.Builder(context, master_key)
                    .setKeyGenParameterSpec(keyGenParameterSpec)
                    .build();

            return (EncryptedSharedPreferences) EncryptedSharedPreferences
                    .create(context,
                            "ACCOUNTS",
                            masterKey,
                            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        }
        catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "wrong key...");
            throw new AEADBadTagException();
        }

        return null;
    }

    public List<Account> getEncryptedAccounts(String master_key) throws AEADBadTagException {
        List<Account> accounts = new ArrayList<>();
        Gson gson = new Gson();

        encryptedSharedPreferences = getEncryptedSharedPreferences(master_key);

        Map<String, ?> all = encryptedSharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : all.entrySet()) {
            if (!entry.getKey().equals("MASTER_KEY")) {
                //Log.d(TAG, "Key: " + entry.getKey() + ", Value: " + String.valueOf(entry.getValue()));
                Account tmpAccount = gson.fromJson(String.valueOf(entry.getValue()), Account.class);
                accounts.add(tmpAccount);
            }
        }

        return accounts;
    }
}
