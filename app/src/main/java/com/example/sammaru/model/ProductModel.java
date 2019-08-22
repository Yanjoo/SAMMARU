package com.example.sammaru.model;

/**
 * 배송 상품 모델
 */

public class ProductModel {
    private String productName;  // 상품명
    private String url;          // 배송조회 url
    private String owner;        // 고객
    private String address;      // 고객 주소
    private String uid;          // 고객 uid 채팅에서 사용
    private String number;       // 송장번호

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
