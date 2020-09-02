package at.mike.accountmanager;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AccountDao {

    @Insert
    void insert(Account account);

    @Update
    void update(Account account);

    @Delete
    void delete(Account account);

    @Query("select * from account order by platform desc")
    LiveData<List<Account>> getAllAccounts();

    @Query("select * from account where platform = :platform")
    Account getAccount(String platform);
}
