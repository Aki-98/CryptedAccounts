package at.mike.accountmanager;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity implements ActivityCallback {

    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.fragment_main, new StartFragment())
                .addToBackStack("StartFragment")
                .commit();
    }

    @Override
    public void onCallback(String master_key, int mode, Account account) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = null;

        switch (mode) {
            case Constants.OPEN_ACC_FRG:
                fragment = new AccountFragment(master_key);
                break;

            case Constants.OPEN_ADD_ACC_FRG:
            case Constants.ADD_NEW_ACC:
                fragment = new AddOrUpdAccountFragment(master_key, Constants.ADD_NEW_ACC, null);
                break;

            case Constants.UPD_ACC:
                fragment = new AddOrUpdAccountFragment(master_key, Constants.UPD_ACC, account);
            default:
                break;
        }

        if (fragment != null) {
            fm.beginTransaction()
                    .add(R.id.fragment_main, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }
}