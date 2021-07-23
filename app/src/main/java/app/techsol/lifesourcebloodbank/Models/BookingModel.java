package app.techsol.lifesourcebloodbank.Models;

public class BookingModel {
    String id;
    String seekerid;
    String donorid;
    String donorname;
    String bloodtype;
    String donationdate;
    String donationstatus;
    String donorstory;

    public String getDonorstory() {
        return donorstory;
    }

    public void setDonorstory(String donorstory) {
        this.donorstory = donorstory;
    }

    public String getSeekerstory() {
        return seekerstory;
    }

    public void setSeekerstory(String seekerstory) {
        this.seekerstory = seekerstory;
    }

    public BookingModel(String id, String seekerid, String donorid, String donorname, String bloodtype, String donationdate, String donationstatus, String donorstory, String seekerstory) {
        this.id = id;
        this.seekerid = seekerid;
        this.donorid = donorid;
        this.donorname = donorname;
        this.bloodtype = bloodtype;
        this.donationdate = donationdate;
        this.donationstatus = donationstatus;
        this.donorstory = donorstory;
        this.seekerstory = seekerstory;
    }

    String seekerstory;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeekerid() {
        return seekerid;
    }

    public void setSeekerid(String seekerid) {
        this.seekerid = seekerid;
    }

    public String getDonorid() {
        return donorid;
    }

    public void setDonorid(String donorid) {
        this.donorid = donorid;
    }

    public String getDonorname() {
        return donorname;
    }

    public void setDonorname(String donorname) {
        this.donorname = donorname;
    }

    public String getBloodtype() {
        return bloodtype;
    }

    public void setBloodtype(String bloodtype) {
        this.bloodtype = bloodtype;
    }

    public String getDonationdate() {
        return donationdate;
    }

    public void setDonationdate(String donationdate) {
        this.donationdate = donationdate;
    }

    public String getDonationstatus() {
        return donationstatus;
    }

    public void setDonationstatus(String donationstatus) {
        this.donationstatus = donationstatus;
    }

    public BookingModel() {
    }

    public BookingModel(String id, String seekerid, String donorid, String donorname, String bloodtype, String donationdate, String donationstatus) {
        this.id = id;
        this.seekerid = seekerid;
        this.donorid = donorid;
        this.donorname = donorname;
        this.bloodtype = bloodtype;
        this.donationdate = donationdate;
        this.donationstatus = donationstatus;
    }
}
