package at.mike.accountmanager;

public class Account {

    private String platform;

    private String username;

    private String email;

    private String password;

    public Account() {
    }

    public Account(String platform, String username, String email, String password) {
        this.platform = platform;
        this.username = username;
        this.email = email;
        this.password = password;
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
}
