package at.mike.accountmanager;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class AccountRepository {
    private AccountDao accountDao;
    private LiveData<List<Account>> allAccounts;

    public AccountRepository(Application application) {
        AccountDatabase accountDatabase = AccountDatabase.getInstance(application);
        accountDao = accountDatabase.accountDao();
        allAccounts = accountDao.getAllAccounts();
    }

    public void insert(Account Account) {
        new InsertAsyncTask(accountDao).execute(Account);
    }

    public void update(Account Account) {
        new UpdateAsyncTask(accountDao).execute(Account);
    }

    public void delete(Account Account) {
        new DeleteAsyncTask(accountDao).execute(Account);
    }

    public LiveData<List<Account>> getAllAccounts() {
        return allAccounts;
    }

    public Account getAccount(String platform) {
        try {
            return new GetAccountAsyncTask(accountDao).execute(platform).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class InsertAsyncTask extends AsyncTask<Account, Void, Void> {

        private AccountDao accountDao;

        public InsertAsyncTask(AccountDao accountDao) {
            this.accountDao = accountDao;
        }

        @Override
        protected Void doInBackground(Account... accounts) {
            accountDao.insert(accounts[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Account, Void, Void> {

        private AccountDao accountDao;

        public UpdateAsyncTask(AccountDao accountDao) {
            this.accountDao = accountDao;
        }

        @Override
        protected Void doInBackground(Account... accounts) {
            accountDao.update(accounts[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Account, Void, Void> {

        private AccountDao accountDao;

        public DeleteAsyncTask(AccountDao accountDao) {
            this.accountDao = accountDao;
        }

        @Override
        protected Void doInBackground(Account... accounts) {
            accountDao.delete(accounts[0]);
            return null;
        }
    }

    private static class GetAccountAsyncTask extends AsyncTask<String, Void, Account> {

        private AccountDao accountDao;

        public GetAccountAsyncTask(AccountDao accountDao) {
            this.accountDao = accountDao;
        }

        @Override
        protected Account doInBackground(String... platforms) {
            return accountDao.getAccount(platforms[0]);
        }
    }
}
