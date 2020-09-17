package at.mike.accountmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

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
    private EncryptedSharedPreferences encryptedSharedPreferences;
    private final int KEY_SIZE = 256;
    private final String FILE_NAME = "ACCOUNTS";
    private final String MASTER_KEY = "MASTER_KEY";

    public EncryptionManager(Context context) {
        this.context = context;
    }

    /**
     * creates encrypted sharedPreferences using the user defined master_key
     * @param master_key key to encrypt/decrypt sharedPreferences
     * @throws AEADBadTagException wrong key exception
     */
    public void createKeyStore(String master_key) throws AEADBadTagException {
        SharedPreferences.Editor editor = getEncryptedSharedPreferences(master_key).edit();
        editor.putString(MASTER_KEY, master_key);
        editor.apply();

        Log.d(TAG, "Encrypted SharedPreferences created...");
    }

    /**
     * write or update an account to encrypted sharedPreferences
     * @param account account to be written/updated
     * @param master_key key to encrypt/decrypt sharedPreferences
     * @throws AEADBadTagException wrong key exception
     */
    public void writeOrUpdateAccount(Account account, String master_key) throws AEADBadTagException {
        encryptedSharedPreferences = getEncryptedSharedPreferences(master_key);

        Gson gson = new Gson();
        SharedPreferences.Editor sharedPrefsEditor = encryptedSharedPreferences.edit();
        sharedPrefsEditor.putString(account.getPlatform(), gson.toJson(account));
        sharedPrefsEditor.apply();

        Log.d(TAG, "Update Account(Platform): " + account.getPlatform());
    }

    /**
     * returns the value of the sharedPreferences key/value pair
     * @param name name of the key
     * @param master_key key to encrypt/decrypt sharedPreferences
     * @return value of the sharedPreferences key/value pair
     * @throws AEADBadTagException wrong key exception
     */
    public String get(String name, String master_key) throws AEADBadTagException {
        String value = "";
        encryptedSharedPreferences = getEncryptedSharedPreferences(master_key);

        if (encryptedSharedPreferences != null) {
            value = encryptedSharedPreferences.getString(name, "default");
        }

        return value;
    }

    /**
     * access the encrypted sharedPreferences
     * @param master_key key to encrypt/decrypt sharedPreferences
     * @return return an object of the encrypted sharedPreferences
     * @throws AEADBadTagException wrong key exception
     */
    private EncryptedSharedPreferences getEncryptedSharedPreferences(String master_key) throws AEADBadTagException {
        try {
            KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder(
                    master_key,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setKeySize(KEY_SIZE)
                    .build();

            MasterKey masterKey = new MasterKey.Builder(context, master_key)
                    .setKeyGenParameterSpec(keyGenParameterSpec)
                    .build();

            return (EncryptedSharedPreferences) EncryptedSharedPreferences
                    .create(context,
                            FILE_NAME,
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

    /**
     * encrypts all stored accounts in sharedPreferences
     * @param master_key key to encrypt/decrypt sharedPreferences
     * @return return a list of encrypted account objects
     * @throws AEADBadTagException wrong key exception
     */
    public List<Account> getEncryptedAccounts(String master_key) throws AEADBadTagException {
        List<Account> accounts = new ArrayList<>();
        Gson gson = new Gson();

        encryptedSharedPreferences = getEncryptedSharedPreferences(master_key);

        if (encryptedSharedPreferences != null) {
            Map<String, ?> all = encryptedSharedPreferences.getAll();

            for (Map.Entry<String, ?> entry : all.entrySet()) {
                if (!entry.getKey().equals(MASTER_KEY)) {
                    //Log.d(TAG, "Key: " + entry.getKey() + ", Value: " + String.valueOf(entry.getValue()));
                    Account tmpAccount = gson.fromJson(String.valueOf(entry.getValue()), Account.class);
                    accounts.add(tmpAccount);
                }
            }
        }

        return accounts;
    }
}
