package at.mike.accountmanager;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "account")
public class Account {

    @PrimaryKey
    @NonNull
    private String platform;

    @ColumnInfo
    private String username;

    @ColumnInfo
    private String email;

    @ColumnInfo
    private String password;

    @ColumnInfo
    private String logo;

    @Ignore
    public Account() {
    }

    public Account(String platform, String username, String email, String password, String logo) {
        this.platform = platform;
        this.username = username;
        this.email = email;
        this.password = password;
        this.logo = logo;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
