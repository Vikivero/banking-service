package com.banking.entity;

import java.io.Serializable;
import java.util.Objects;

public class BankAccount implements Serializable {
    private String firstName = "";
    private String lastName = "";
    private long pesel;
    private long accountNumber;
    private double accountBalance;

    public BankAccount(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BankAccount(String firstName, String lastName, long pesel, long accountNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = pesel;
        this.accountNumber = accountNumber;
        this.accountBalance = 0;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
        BankAccount bankAccount = (BankAccount) o;
        return pesel == bankAccount.pesel && accountNumber == bankAccount.accountNumber && Double.compare(bankAccount.accountBalance, accountBalance) == 0 && Objects.equals(firstName, bankAccount.firstName) && Objects.equals(lastName, bankAccount.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, pesel, accountNumber, accountBalance);
    }
}
