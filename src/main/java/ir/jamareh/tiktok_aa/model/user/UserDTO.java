package ir.jamareh.tiktok_aa.model.user;

public class UserDTO {
    private long id;
    private String username;
    private boolean enabled;

    public UserDTO(long id,String username, boolean enabled) {
        this.id = id;
        this.username = username;
        this.enabled = enabled;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
