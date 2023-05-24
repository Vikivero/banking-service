package com.banking.entity;

import java.util.Objects;

public class User {
    private String name;
    private String surname;
    private long pesel;
    private long accountNumber;
    private double accountBalance;

    public User(String name, String surname, long pesel, long accountNumber, double accountBalance) {
        this.name = name;
        this.surname = surname;
        this.pesel = pesel;
        this.accountNumber = accountNumber;
        this.accountBalance = accountBalance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public long getPesel() {
        return pesel;
    }

    public void setPesel(long pesel) {
        this.pesel = pesel;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    //przez alt+ insert, ctrl+o to nie autogeneracja
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return pesel == user.pesel && accountNumber == user.accountNumber && Double.compare(user.accountBalance, accountBalance) == 0 && Objects.equals(name, user.name) && Objects.equals(surname, user.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, pesel, accountNumber, accountBalance);
    }
}
