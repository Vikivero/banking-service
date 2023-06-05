package com.banking.entity;

import java.io.Serializable;
import java.util.Objects;

public class Credentials implements Serializable {

    private long accountNumber;
    private String password;

    //pojo - plain old java object
    public Credentials(long accountNumber, String password) {
        this.accountNumber = accountNumber;
        this.password = password;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Credentials credentials = (Credentials) o;
        return accountNumber == credentials.accountNumber && Objects.equals(password, credentials.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, password);
    }
}
