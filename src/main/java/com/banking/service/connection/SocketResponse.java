package com.banking.service.connection;

import java.io.Serializable;
import java.util.Objects;

public class SocketResponse implements Serializable {
    private boolean isSuccessful;
    private String errorMessage = "";
    private Object data;
    public SocketResponse(boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    public SocketResponse(boolean isSuccessful, String errorMessage) {
        this.isSuccessful = isSuccessful;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SocketResponse that = (SocketResponse) o;
        return isSuccessful == that.isSuccessful && Objects.equals(errorMessage, that.errorMessage) && Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isSuccessful, errorMessage, data);
    }
}
