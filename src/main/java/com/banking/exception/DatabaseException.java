package com.banking.exception;

import java.io.IOException;

public class DatabaseException extends Exception {
    public DatabaseException(String s) {
        super(s);
    }

    public DatabaseException(Throwable e) {
        super(e);
    }
}
