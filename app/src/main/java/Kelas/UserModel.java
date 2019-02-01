package Kelas;

/**
 * Created by Glory on 02/09/2018.
 */

public class UserModel {
    public String uid;
    public String displayName;
    public String token;
    public String last_login;
    public String check;
    public String phone;
    public String level;
    public String email;
    public String foto;

    public UserModel(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public UserModel(String uid, String displayName, String token, String last_login, String check, String phone,String level,
                     String email,String foto) {
        this.uid = uid;
        this.displayName = displayName;
        this.token = token;
        this.last_login = last_login;
        this.check = check;
        this.phone = phone;
        this.level = level;
        this.email = email;
        this.foto = foto;
    }

    public String getUid() {
        return uid;
    }


    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }
}
