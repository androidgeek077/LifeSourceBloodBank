package app.techsol.lifesourcebloodbank.Models;

public class UserModel {
    String userid, name, email, phoneno, bloodtype, userlat, userlong;

    public UserModel(String userid, String name, String email, String phoneno, String bloodtype, String userlat, String userlong) {
        this.userid = userid;
        this.name = name;
        this.email = email;
        this.phoneno = phoneno;
        this.bloodtype = bloodtype;
        this.userlat = userlat;
        this.userlong = userlong;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getBloodtype() {
        return bloodtype;
    }

    public void setBloodtype(String bloodtype) {
        this.bloodtype = bloodtype;
    }

    public String getUserlat() {
        return userlat;
    }

    public void setUserlat(String userlat) {
        this.userlat = userlat;
    }

    public String getUserlong() {
        return userlong;
    }

    public void setUserlong(String userlong) {
        this.userlong = userlong;
    }
}
