package com.banking.repository;

import com.banking.entity.Credentials;
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

public class CredentialsStorage {
    private static final String CREDENTIALS_DATABASE = "./creds/db.txt";
    private final Gson gson;

    public CredentialsStorage() throws DatabaseException {
        this.gson = new GsonBuilder().setPrettyPrinting().create();

        createDb();
    }

    //crud
//    public Credentials create(Credentials credentials) throws DatabaseException, BankAccountException {
//        File dbFile = new File(CREDENTIALS_DATABASE);
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
//            Type type = new TypeToken<Map<Long, Credentials>>() {
//            }.getType();
//            Map<Long, Credentials> mapFromJson = gson.fromJson(sdb, type);
//
//            if (mapFromJson.containsKey(credentials.getAccountNumber())) {
//                throw new BankAccountException((String.format(
//                        "Credentials with bank account number %s already exist!",
//                        credentials.getAccountNumber())));
//            }
//
//            // Save value to map
//            mapFromJson.put(credentials.getAccountNumber(), credentials);
//
//            // Serialize map
//            String updatedSdb = gson.toJson(mapFromJson);
//
//            // Save back to file
//            FileUtils.writeWithFileChannel(channel, updatedSdb, CREDENTIALS_DATABASE);
//
//            lock.release();
//
//            return credentials;
//        } catch (IOException e) {
//            throw new DatabaseException("Account Database file is not accessible or does not exist!");
//        }
//    }

    public Credentials read(long accountNumber) throws IllegalStateException, DatabaseException {
        File dbFile = new File(CREDENTIALS_DATABASE);
        if (!dbFile.exists()) {
            throw new DatabaseException("Credentials Database is not exist!");
        }

        try (RandomAccessFile file = new RandomAccessFile(dbFile, "rw");
             FileChannel channel = file.getChannel();
             FileLock lock = channel.lock()) {

            // Read file database from file
            String sdb = FileUtils.readWithFileChannel(channel);
            lock.release();

            // Convert the JSON string back to a map
            Type type = new TypeToken<Map<Long, Credentials>>() {
            }.getType();
            Map<Long, Credentials> mapFromJson = gson.fromJson(sdb, type);

            return mapFromJson.getOrDefault(accountNumber, null);
        } catch (IOException e) {
            throw new DatabaseException("Account Database file is not accessible or does not exist!");
        }
    }

    public Credentials update(Credentials credentials) throws DatabaseException, BankAccountException {
        File dbFile = new File(CREDENTIALS_DATABASE);

        if (!dbFile.exists()) {
            throw new DatabaseException("Credentials Database is not exist!");
        }

        try (RandomAccessFile file = new RandomAccessFile(dbFile, "rw");
             FileChannel channel = file.getChannel();
             FileLock lock = channel.lock()) {

            // Read file database from file
            String sdb = FileUtils.readWithFileChannel(channel);

            // Convert the JSON string back to a map
            Type type = new TypeToken<Map<Long, Credentials>>() {
            }.getType();
            Map<Long, Credentials> mapFromJson = gson.fromJson(sdb, type);

            mapFromJson.put(credentials.getAccountNumber(), credentials);

            FileUtils.writeWithFileChannel(channel, gson.toJson(mapFromJson), CREDENTIALS_DATABASE);
            lock.release();

            return credentials;
        } catch (IOException e) {
            throw new DatabaseException("Account Database file is not accessible or does not exist!");
        }
    }

    public void delete(long accountNumber) throws DatabaseException {
        File dbFile = new File(CREDENTIALS_DATABASE);
        if (!dbFile.exists()) {
            throw new DatabaseException("Credentials Database is not exist!");
        }

        try (RandomAccessFile file = new RandomAccessFile(dbFile, "rw");
             FileChannel channel = file.getChannel();
             FileLock lock = channel.lock()) {

            // Read file database from file
            String sdb = FileUtils.readWithFileChannel(channel);

            // Convert the JSON string back to a map
            Type type = new TypeToken<Map<Long, Credentials>>() {
            }.getType();
            Map<Long, Credentials> mapFromJson = gson.fromJson(sdb, type);
            mapFromJson.remove(accountNumber);

            FileUtils.writeWithFileChannel(channel, gson.toJson(mapFromJson), CREDENTIALS_DATABASE);
            lock.release();
        } catch (IOException e) {
            throw new DatabaseException("Account Database file is not accessible or does not exist!");
        }
    }

    private void createDb() throws DatabaseException {
        File dbFile = new File(CREDENTIALS_DATABASE);
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
            Map<Long, Credentials> mapFromJson = new HashMap<>();
            mapFromJson.put(11111111L, new Credentials(11111111L, "password"));
            mapFromJson.put(22222222L, new Credentials(22222222L, "password"));

            FileUtils.writeWithFileChannel(channel, gson.toJson(mapFromJson), CREDENTIALS_DATABASE);
            lock.release();

        } catch (IOException e) {
            e.printStackTrace();
            throw new DatabaseException("Account Database file is not accessible or does not exist!");
        }
    }
}
