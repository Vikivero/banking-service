package com.banking.repository;

import com.banking.entity.BankAccount;
import com.banking.exception.BankAccountException;
import com.banking.exception.DatabaseException;
import com.banking.utils.FileUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Type;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.HashMap;
import java.util.Map;

public class BankAccountStorage {
    private static final String ACCOUNTS_DATABASE = "./accounts/db.txt";
    private final Gson gson;

    public BankAccountStorage() throws DatabaseException {

        this.gson = new GsonBuilder().setPrettyPrinting().create();

        createDb();
    }

    //crud
//    public BankAccount create(BankAccount bankAccount) throws DatabaseException {
//        File dbFile = new File(ACCOUNTS_DATABASE);
//
//        if (!dbFile.exists()) {
//            throw new DatabaseException("Account Database is not exist!");
//        }
//
//        try (RandomAccessFile file = new RandomAccessFile(dbFile, "rw");
//             FileChannel channel = file.getChannel();
//             FileLock lock = channel.lock()) {
//
//            // Read file database from file
//            String sdb = FileUtils.readWithFileChannel(channel);
//
//            // Convert the JSON string back to a map
//            Type type = new TypeToken<Map<Long, BankAccount>>() {
//            }.getType();
//            Map<Long, BankAccount> mapFromJson = gson.fromJson(sdb, type);
//
//            if (mapFromJson.containsKey(bankAccount.getAccountNumber())) {
//                throw new BankAccountException((String.format(
//                        "Account with number %s already exists!",
//                        bankAccount.getAccountNumber())));
//            }
//
//            // Save value to map
//            mapFromJson.put(bankAccount.getAccountNumber(), bankAccount);
//
//            // Serialize map
//            String updatedSdb = gson.toJson(mapFromJson);
//
//            // Save back to file
//            FileUtils.writeWithFileChannel(channel, updatedSdb, ACCOUNTS_DATABASE);
//
//            lock.release();
//
//            return bankAccount;
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        return null;
//    }

    public BankAccount read(long accountNumber) throws DatabaseException {
        File dbFile = new File(ACCOUNTS_DATABASE);
        try {
            if (!dbFile.exists()) {
                throw new DatabaseException("Account Database is not exist!");
            }

            try (RandomAccessFile file = new RandomAccessFile(dbFile, "rw");
                 FileChannel channel = file.getChannel();
                 FileLock lock = channel.lock()) {

                // Read file database from file
                String sdb = FileUtils.readWithFileChannel(channel);
                lock.release();

                // Convert the JSON string back to a map
                Type type = new TypeToken<Map<Long, BankAccount>>() {
                }.getType();
                Map<Long, BankAccount> mapFromJson = gson.fromJson(sdb, type);

                return mapFromJson.getOrDefault(accountNumber, null);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public BankAccount save(BankAccount bankAccount) throws DatabaseException, BankAccountException {
        File dbFile = new File(ACCOUNTS_DATABASE);

        if (!dbFile.exists()) {
            throw new DatabaseException("Account Database is not exist!");
        }

        try (RandomAccessFile file = new RandomAccessFile(dbFile, "rw");
             FileChannel channel = file.getChannel();
             FileLock lock = channel.lock()) {

            // Read file database from file
            String sdb = FileUtils.readWithFileChannel(channel);

            // Convert the JSON string back to a map
            Type type = new TypeToken<Map<Long, BankAccount>>() {
            }.getType();
            Map<Long, BankAccount> mapFromJson = gson.fromJson(sdb, type);

            BankAccount targetAccount = mapFromJson.getOrDefault(bankAccount.getAccountNumber(), null);
            if (targetAccount == null) {

                // 8 digits for valid acc.num.
                long nextAccNumber = 10000000L + mapFromJson.size();
                targetAccount = new BankAccount(nextAccNumber);
            } else {
                targetAccount = bankAccount;
            }

            mapFromJson.put(targetAccount.getAccountNumber(), targetAccount);

            FileUtils.writeWithFileChannel(channel, gson.toJson(mapFromJson), ACCOUNTS_DATABASE);
            lock.release();
            return targetAccount;
        } catch (IOException e) {
            throw new DatabaseException("Account Database file is not accessible or does not exist!");
        }
    }

    public void delete(long accountNumber) throws DatabaseException {
        File dbFile = new File(ACCOUNTS_DATABASE);
        if (!dbFile.exists()) {
            throw new DatabaseException("Account Database is not exist!");
        }

        try (RandomAccessFile file = new RandomAccessFile(dbFile, "rw");
             FileChannel channel = file.getChannel();
             FileLock lock = channel.lock()) {

            // Read file database from file
            String sdb = FileUtils.readWithFileChannel(channel);

            // Convert the JSON string back to a map
            Type type = new TypeToken<Map<Long, BankAccount>>() {
            }.getType();
            Map<Long, BankAccount> mapFromJson = gson.fromJson(sdb, type);
            mapFromJson.remove(accountNumber);

            FileUtils.writeWithFileChannel(channel, gson.toJson(mapFromJson), ACCOUNTS_DATABASE);
            lock.release();
        } catch (IOException e) {
            throw new DatabaseException("Account Database file is not accessible or does not exist!");
        }
    }

    public BankAccount withdraw(long accountNumber, double amount) throws DatabaseException, BankAccountException {
        File dbFile = new File(ACCOUNTS_DATABASE);

        if (!dbFile.exists()) {
            throw new DatabaseException("Account Database is not exist!");
        }

        try (RandomAccessFile file = new RandomAccessFile(dbFile, "rw");
             FileChannel channel = file.getChannel();
             FileLock lock = channel.lock()) {

            // Read file database from file
            String sdb = FileUtils.readWithFileChannel(channel);

            // Convert the JSON string back to a map
            Type type = new TypeToken<Map<Long, BankAccount>>() {
            }.getType();
            Map<Long, BankAccount> mapFromJson = gson.fromJson(sdb, type);

            BankAccount existing = mapFromJson.getOrDefault(accountNumber, null);
            if (existing == null) {
                lock.release();
                throw new BankAccountException((String.format(
                        "Account with number %s does not exist!",
                        accountNumber)));
            }

            if (amount > existing.getAccountBalance()) {
                lock.release();
                throw new BankAccountException("Not enough money!");
            } else {
                existing.setAccountBalance(existing.getAccountBalance() - amount);
            }

            FileUtils.writeWithFileChannel(channel, gson.toJson(mapFromJson), ACCOUNTS_DATABASE);
            lock.release();
            return existing;

        } catch (IOException e) {
            throw new DatabaseException("Account Database file is not accessible or does not exist!");
        }
    }

    public BankAccount deposit(long accountNumber, double amount) throws DatabaseException, BankAccountException {
        File dbFile = new File(ACCOUNTS_DATABASE);

        if (!dbFile.exists()) {
            throw new DatabaseException("Account Database is not exist!");
        }

        try (RandomAccessFile file = new RandomAccessFile(dbFile, "rw");
             FileChannel channel = file.getChannel();
             FileLock lock = channel.lock()) {

            // Read file database from file
            String sdb = FileUtils.readWithFileChannel(channel);

            // Convert the JSON string back to a map
            Type type = new TypeToken<Map<Long, BankAccount>>() {
            }.getType();
            Map<Long, BankAccount> mapFromJson = gson.fromJson(sdb, type);

            BankAccount existing = mapFromJson.getOrDefault(accountNumber, null);
            if (existing == null) {
                lock.release();
                throw new BankAccountException((String.format(
                        "Account with number %s does not exist!",
                        accountNumber)));
            }

            existing.setAccountBalance(existing.getAccountBalance() + amount);

            FileUtils.writeWithFileChannel(channel, gson.toJson(mapFromJson), ACCOUNTS_DATABASE);
            lock.release();

            return existing;

        } catch (IOException e) {
            throw new DatabaseException("Account Database file is not accessible or does not exist!");
        }
    }

    public BankAccount transfer(long sourceAccountNumber, long targetAccountNumber, double amount) throws DatabaseException, BankAccountException {
        File dbFile = new File(ACCOUNTS_DATABASE);

        if (!dbFile.exists()) {
            throw new DatabaseException("Account Database is not exist!");
        }

        try (RandomAccessFile file = new RandomAccessFile(dbFile, "rw");
             FileChannel channel = file.getChannel();
             FileLock lock = channel.lock()) {

            // Read file database from file
            String sdb = FileUtils.readWithFileChannel(channel);

            // Convert the JSON string back to a map
            Type type = new TypeToken<Map<Long, BankAccount>>() {
            }.getType();
            Map<Long, BankAccount> mapFromJson = gson.fromJson(sdb, type);

            BankAccount sourceAccount = mapFromJson.getOrDefault(sourceAccountNumber, null);
            BankAccount targetAccount = mapFromJson.getOrDefault(targetAccountNumber, null);

            if (sourceAccount == null || targetAccount == null) {
                lock.release();
                throw new BankAccountException("Account does not exist!");
            }

            if (sourceAccount.getAccountBalance() < amount) {
                lock.release();
                throw new BankAccountException("Not enough money!");
            } else {
                sourceAccount.setAccountBalance(sourceAccount.getAccountBalance() - amount);
                targetAccount.setAccountBalance(targetAccount.getAccountBalance() + amount);

                System.out.println("Transfer successful");

                FileUtils.writeWithFileChannel(channel, gson.toJson(mapFromJson), ACCOUNTS_DATABASE);
                lock.release();
            }

            return sourceAccount;

        } catch (IOException e) {
            throw new DatabaseException("Account Database file is not accessible or does not exist!");
        }
    }

    private void createDb() throws DatabaseException {
        File dbFile = new File(ACCOUNTS_DATABASE);
        if (dbFile.exists()) {
            return;
        }

        if (!dbFile.exists() && dbFile.getParentFile() != null && !dbFile.getParentFile().mkdirs()) {
            try {
                dbFile.createNewFile();
            } catch (IOException e) {
                throw new DatabaseException("Account Database file is not accessible or does not exist!");
            }
        }

        try (RandomAccessFile file = new RandomAccessFile(dbFile, "rw");
             FileChannel channel = file.getChannel();
             FileLock lock = channel.lock()) {

            // Convert the JSON string back to a map
            Map<Long, BankAccount> mapFromJson = new HashMap<>();
            BankAccount testAcc1 = new BankAccount("Developer1", "Testing1", 11111111111L, 11111111L);
            testAcc1.setAccountBalance(100.50);
            mapFromJson.put(11111111L, testAcc1);

            BankAccount testAcc2 = new BankAccount("Developer2", "Testing2",22222222222L, 22222222L);
            testAcc2.setAccountBalance(10.00);
            mapFromJson.put(22222222L, testAcc2);

            FileUtils.writeWithFileChannel(channel, gson.toJson(mapFromJson), ACCOUNTS_DATABASE);
            lock.release();

        } catch (IOException e) {
            throw new DatabaseException("Account Database file is not accessible or does not exist!");
        }
    }
}
