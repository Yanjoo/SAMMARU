package com.example.sammaru.model;

/**
 * 어플 사용자 모델
 * 구분자 1 : 고객, 2: 배송원
 */

public class UserModel {
    private String name;    // 사용자 이름
    private String uid;     // 사용자 uid
    private String phone;   // 사용자 전화번호
    private String address; // 사용자 주소 (배달원 null)
    private String company; // 사용자 회사 (고객 null)
    private int identifier; // 1이면 고객 2이면 배달원
    public String pushToken;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
