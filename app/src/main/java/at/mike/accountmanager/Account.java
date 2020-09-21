package at.mike.accountmanager;

import android.net.Uri;

public class Account {

    private String platform;

    private String username;

    private String email;

    private String password;

    private String logo;

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
