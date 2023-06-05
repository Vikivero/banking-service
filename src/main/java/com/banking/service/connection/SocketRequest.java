package com.banking.service.connection;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SocketRequest implements Serializable {
    private final long accountNumber;
    private final RequestType requestType;
    private Map<String, Object> body;

    public SocketRequest(long accountNumber, RequestType requestType) {
        this.accountNumber = accountNumber;
        this.requestType = requestType;
        this.body = new HashMap<>();
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public enum RequestType {
        DEPOSIT,
        WITHDRAW,
        TRANSFER,
        GETACCOUNT,
        AUTHENTICATION,
        SAVEACCOUNT,
        DELETEACCOUNT,
        SAVECREDENTIALS,
        DELETECREDENTIALS,
        UNKNOWN
    }
}
