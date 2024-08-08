package com.jio.cohorts.pojo;

public class Member {
    private int memberid;
    private  String fullname;

    private String rilemailid;

    private int contactnumber;
    private int fk_userid;

    public int getMemberid() {
        return memberid;
    }

    public void setMemberid(int memberid) {
        this.memberid = memberid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getRilemailid() {
        return rilemailid;
    }

    public void setRilemailid(String rilemailid) {
        this.rilemailid = rilemailid;
    }

    public int getContactnumber() {
        return contactnumber;
    }

    public void setContactnumber(int contactnumber) {
        this.contactnumber = contactnumber;
    }

    public int getFk_userid() {
        return fk_userid;
    }

    public void setFk_userid(int fk_userid) {
        this.fk_userid = fk_userid;
    }





}
