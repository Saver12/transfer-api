package com.saver12.db;

import com.saver12.dto.AccountDTO;
import com.saver12.dto.UserDTO;
import com.saver12.model.Account;
import com.saver12.model.Transfer;
import com.saver12.model.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.saver12.service.impl.TransferServiceImpl.TRANSFER_FAILED;
import static com.saver12.service.impl.TransferServiceImpl.TRANSFER_SUCCESS;

public class DBStore {

    private static final Map<Long, User> users = new ConcurrentHashMap<>();
    private static final Map<Long, Account> accounts = new ConcurrentHashMap<>();
    private static final Map<Long, Transfer> transfers = new ConcurrentHashMap<>();

    static {
        createUsers();
        createAccounts();
        createTransfers();
    }

    public static List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public static User getUser(long userId) {
        return users.get(userId);
    }

    public static void addUser(User user) {
        users.put(user.getId(), user);
    }

    public static User updateUser(long id, UserDTO user) {
        User updated = null;
        if (users.containsKey(id)) {
            updated = new User(id, user.getName(), user.getEmail());
            users.put(id, updated);
        }
        return updated;
    }

    public static User deleteUser(long id) {
        return users.remove(id);
    }

    public static List<Account> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }

    public static Account getAccount(long id) {
        return accounts.get(id);
    }

    public static void addAccount(Account account) {
        accounts.put(account.getId(), account);
    }

    public static Account updateAccount(long id, AccountDTO account) {
        Account updated = null;
        if (accounts.containsKey(id)) {
            updated = new Account(id, accounts.get(id).getUserId(), account.getBalance(),
                    Currency.getInstance(account.getCurrency()));
            accounts.put(id, updated);
        }
        return updated;
    }

    public static Account deleteAccount(long id) {
        return accounts.remove(id);
    }

    public static List<Transfer> getAllTransfers() {
        return new ArrayList<>(transfers.values());
    }

    public static Transfer getTransfer(long id) {
        return transfers.get(id);
    }

    public static void addTransfer(Transfer transfer) {
        transfers.put(transfer.getId(), transfer);
    }

    public static Transfer deleteTransfer(long id) {
        return transfers.remove(id);
    }

    private static void createUsers() {
        addUser(new User(1, "John", "jd@mail.com"));
        addUser(new User(2, "Max", "balloon@yahoo.com"));
        addUser(new User(3, "David", "zakk@yandex.ru"));
    }

    private static void createAccounts() {
        addAccount(new Account(1, 1, BigDecimal.valueOf(400), Currency.getInstance("USD")));
        addAccount(new Account(2, 1, BigDecimal.valueOf(150), Currency.getInstance("GBP")));
        addAccount(new Account(3, 2, BigDecimal.valueOf(800), Currency.getInstance("USD")));
        addAccount(new Account(4, 2, BigDecimal.valueOf(350), Currency.getInstance("AUD")));
        addAccount(new Account(5, 2, BigDecimal.valueOf(300), Currency.getInstance("GBP")));
    }

    private static void createTransfers() {
        addTransfer(new Transfer(1, 1, 3,
                Currency.getInstance("USD"), BigDecimal.valueOf(100), Transfer.Status.EXECUTED, TRANSFER_SUCCESS));
        addTransfer(new Transfer(2, 3, 1,
                Currency.getInstance("USD"), BigDecimal.valueOf(300), Transfer.Status.EXECUTED, TRANSFER_SUCCESS));
        addTransfer(new Transfer(3, 1, 2,
                Currency.getInstance("GBP"), BigDecimal.valueOf(50), Transfer.Status.FAILED, TRANSFER_FAILED));
    }
}
