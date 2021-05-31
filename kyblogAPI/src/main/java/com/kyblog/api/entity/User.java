package com.kyblog.api.entity;

public class User {
    Long uid;
    String account;
    String password;
    String salt;
    String headerUrl;
    String qrCodeUrl;
    Integer status;

    @Override
    public String toString() {
        return "user{" +
                "uid=" + uid +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", headerUrl='" + headerUrl + '\'' +
                ", qrCodeUrl='" + qrCodeUrl + '\'' +
                ", status=" + status +
                '}';
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getHeaderUrl() {
        return headerUrl;
    }

    public void setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
