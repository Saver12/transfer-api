package com.saver12.service;

import com.saver12.dto.AccountDTO;
import com.saver12.model.Account;

import java.util.List;

public interface AccountService {

    List<Account> getAllAccounts();

    Account getAccount(Long id);

    Account saveAccount(AccountDTO account);

    Account updateAccount(Long id, AccountDTO account);

    Account deleteAccount(Long id);
}
