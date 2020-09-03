package at.mike.accountmanager;


import android.app.Application;
import android.app.Presentation;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class AccountViewModel extends AndroidViewModel {

    private AccountRepository repository;
    private LiveData<List<Account>> accounts;

    public AccountViewModel(@NonNull Application application) {
        super(application);

        repository = new AccountRepository(application);
        accounts = repository.getAllAccounts();
    }

    public void insert(Account account) {
        repository.insert(account);
    }

    public void delete(Account account) {
        repository.delete(account);
    }

    public void update(Account account) {
        repository.update(account);
    }

    public LiveData<List<Account>> getAllAccounts() {
        return repository.getAllAccounts();
    }
}
