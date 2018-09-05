package com.saver12.service.impl;

import com.saver12.db.DBStore;
import com.saver12.dto.AccountDTO;
import com.saver12.exception.AppException;
import com.saver12.model.Account;
import com.saver12.service.AccountService;
import com.saver12.service.UserService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Currency;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static com.saver12.service.impl.UserServiceImpl.EMPTY_REQUEST_BODY;

@Singleton
public class AccountServiceImpl implements AccountService {

    private static final AtomicLong GENERATED_ID = new AtomicLong(6);
    public static final String ACCOUNT_NOT_FOUND = "account not found";
    public static final String MISSING_UPDATE_VALUES = "JSON should contain balance and currency values";
    public static final String MISSING_SAVE_VALUES = "JSON should contain balance, currency and userId values";
    public static final String UPDATE_NO_ID = "update request should not contain userId value";

    private final UserService userService;

    @Inject
    public AccountServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<Account> getAllAccounts() {
        return DBStore.getAllAccounts();
    }

    @Override
    public Account getAccount(Long id) {
        Account account = DBStore.getAccount(id);
        if (account == null) {
            throw new AppException(ACCOUNT_NOT_FOUND);
        }
        return account;
    }

    @Override
    public Account saveAccount(AccountDTO account) {
        checkRequestBody(account);
        Account saved = new Account(GENERATED_ID.getAndIncrement(), account.getUserId(),
                account.getBalance(), Currency.getInstance(account.getCurrency()));
        DBStore.addAccount(saved);
        return saved;
    }

    @Override
    public Account updateAccount(Long id, AccountDTO account) {
        checkUpdateRequestBody(account);
        Account updated = DBStore.updateAccount(id, account);
        if (updated == null) {
            throw new AppException(ACCOUNT_NOT_FOUND);
        }
        return updated;
    }

    @Override
    public Account deleteAccount(Long id) {
        Account deleted = DBStore.deleteAccount(id);
        if (deleted == null) {
            throw new AppException(ACCOUNT_NOT_FOUND);
        }
        return deleted;
    }

    private void checkRequestBody(AccountDTO account) {
        if (account == null) {
            throw new AppException(EMPTY_REQUEST_BODY);
        } else if (account.getBalance() == null || account.getCurrency() == null || account.getUserId() == null) {
            throw new AppException(MISSING_SAVE_VALUES);
        }
        // if User is not present, exception is thrown
        userService.getUser(account.getUserId());
    }

    private void checkUpdateRequestBody(AccountDTO account) {
        if (account == null) {
            throw new AppException(EMPTY_REQUEST_BODY);
        } else if (account.getBalance() == null || account.getCurrency() == null) {
            throw new AppException(MISSING_UPDATE_VALUES);
        } else if (account.getUserId() != null) {
            throw new AppException(UPDATE_NO_ID);
        }
    }
}
