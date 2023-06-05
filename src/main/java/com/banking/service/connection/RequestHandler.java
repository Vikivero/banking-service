package com.banking.service.connection;

import com.banking.entity.BankAccount;
import com.banking.entity.Credentials;
import com.banking.exception.BankAccountException;
import com.banking.exception.DatabaseException;
import com.banking.repository.CredentialsStorage;
import com.banking.repository.BankAccountStorage;

public class RequestHandler {
    private final CredentialsStorage credentialsStorage;
    private final BankAccountStorage bankAccountStorage;

    public RequestHandler() {
        try {
            this.credentialsStorage = new CredentialsStorage();
            this.bankAccountStorage = new BankAccountStorage();
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    public SocketResponse handleRequest(SocketRequest request) {
        return switch (request.getRequestType()) {
            case DEPOSIT -> handleDepositRequest(request);
            case WITHDRAW -> handleWithdrawRequest(request);
            case TRANSFER -> handleTransferRequest(request);
            case GETACCOUNT -> handleGetUserRequest(request);
            case AUTHENTICATION -> handleAuthenticationRequest(request);
            case SAVEACCOUNT -> handleSaveAccountRequest(request);
            case DELETEACCOUNT -> handleDeleteAccountRequest(request);
            case SAVECREDENTIALS -> handleSaveCredentialsRequest(request);
            case DELETECREDENTIALS -> handleDeleteCredentialsRequest(request);
            default -> handleFailedRequest(request);
        };
    }

    private SocketResponse handleDeleteCredentialsRequest(SocketRequest request) {
        try {
            credentialsStorage.delete(request.getAccountNumber());
        } catch (DatabaseException e) {
            return new SocketResponse(false, e.getMessage());
        }

        return new SocketResponse(true);
    }

    private SocketResponse handleSaveCredentialsRequest(SocketRequest request) {
        String password = (String) request.getBody().get("password");

        try {
            credentialsStorage.update(new Credentials(request.getAccountNumber(), password));
        } catch (DatabaseException | BankAccountException e) {
            return new SocketResponse(false, e.getMessage());
        }

        return new SocketResponse(true);
    }

    private SocketResponse handleDeleteAccountRequest(SocketRequest request) {
        try {
            bankAccountStorage.delete(request.getAccountNumber());
        } catch (DatabaseException e) {
            return new SocketResponse(false, e.getMessage());
        }

        return new SocketResponse(true);
    }

    private SocketResponse handleSaveAccountRequest(SocketRequest request) {
        BankAccount received = (BankAccount) request.getBody().get("account");

        try {
            SocketResponse successfulResponse = new SocketResponse(true);
            successfulResponse.setData(bankAccountStorage.save(received));

            return successfulResponse;
        } catch (DatabaseException | BankAccountException e) {
            return new SocketResponse(false, e.getMessage());
        }
    }

    private SocketResponse handleFailedRequest(SocketRequest request) {
        return new SocketResponse(false, "Unknown Error");
    }

    private SocketResponse handleDepositRequest(SocketRequest request) {
        double amount = (double) request.getBody().get("amount");

        BankAccount ba = null;
        try {
            ba = bankAccountStorage.deposit(
                    request.getAccountNumber(),
                    amount);
        } catch (DatabaseException | BankAccountException e) {
            return new SocketResponse(false, e.getMessage());
        }

        if (ba != null) {
            return new SocketResponse(true);
        } else {
            return new SocketResponse(false, "Unknown Error");
        }
    }

    private SocketResponse handleWithdrawRequest(SocketRequest request) {
        double amount = (double) request.getBody().get("amount");

        BankAccount ba = null;
        try {
            ba = bankAccountStorage.withdraw(
                    request.getAccountNumber(),
                    amount);
        } catch (DatabaseException | BankAccountException e) {
            return new SocketResponse(false, e.getMessage());
        }

        if (ba != null) {
            return new SocketResponse(true);
        } else {
            return new SocketResponse(false, "Unknown Error");
        }
    }

    private SocketResponse handleTransferRequest(SocketRequest request) {
        double amount = (double) request.getBody().get("amount");
        long targetAccountNumber = (long) request.getBody().get("targetAccountNumber");

        BankAccount sourceBa = null;
        try {
            sourceBa = bankAccountStorage.transfer(
                    request.getAccountNumber(),
                    targetAccountNumber,
                    amount);
        } catch (DatabaseException | BankAccountException e) {
            return new SocketResponse(false, e.getMessage());
        }

        if (sourceBa != null) {
            return new SocketResponse(true);
        } else {
            return new SocketResponse(false, "Unknown Error");
        }
    }

    private SocketResponse handleGetUserRequest(SocketRequest request) {
        BankAccount sourceBa;
        try {
            sourceBa = bankAccountStorage.read(request.getAccountNumber());
        } catch (DatabaseException e) {
            return new SocketResponse(false, e.getMessage());
        }

        if (sourceBa != null) {
            SocketResponse response = new SocketResponse(true);
            response.setData(sourceBa);

            return response;
        } else {
            return new SocketResponse(false, "Unknown Error");
        }
    }

    private SocketResponse handleAuthenticationRequest(SocketRequest request) {
        Credentials creds;

        try {
            creds = credentialsStorage.read(request.getAccountNumber());
        } catch (DatabaseException e) {
            return new SocketResponse(false, e.getMessage());
        }

        if (creds != null) {
            String enteredPassword = (String) request.getBody().get("password");
            if (enteredPassword.equals(creds.getPassword())) {
                return new SocketResponse(true);
            } else {
                return new SocketResponse(false, "Wrong Credentials");
            }
        } else {
            return new SocketResponse(false, "Account not found");
        }
    }
}
