package login.register.model;

public class UserBean {

    private int id;
    private String username;
    private String password;
    private String email;
    private String notes;
    private String photo;

    public UserBean(int id, String username, String password, String email,
                    String notes, String photo) {

        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.notes = notes;
        this.photo = photo;
    }

    public UserBean(int id, String userName, String photo) {

        this.id = id;
        this.username = userName;
        this.photo = photo;
    }

    public UserBean(String username, String password, String email,
                    String notes) {

        this.username = username;
        this.password = password;
        this.email = email;
        this.notes = notes;
    }

    public UserBean() {


    }

    public int getId() {

        return id;
    }

    public void setUsername(int id) {

        this.id = id;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}

