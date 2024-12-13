package at.mike.accountmanager;

public interface ActivityCallback {
    void onCallback(String master_key, int mode, Account account);
}
