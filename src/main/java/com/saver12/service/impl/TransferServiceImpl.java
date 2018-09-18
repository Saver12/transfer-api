package com.saver12.service.impl;

import com.saver12.db.DBStore;
import com.saver12.dto.AccountDTO;
import com.saver12.dto.TransferDTO;
import com.saver12.exception.AppException;
import com.saver12.model.Account;
import com.saver12.model.Transfer;
import com.saver12.model.Transfer.Status;
import com.saver12.service.AccountService;
import com.saver12.service.TransferService;
import com.saver12.service.UserService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static com.saver12.service.impl.UserServiceImpl.EMPTY_REQUEST_BODY;

@Singleton
public class TransferServiceImpl implements TransferService {

    private static final AtomicLong GENERATED_ID = new AtomicLong(4);
    public static final String TRANSFER_NOT_FOUND = "transfer not found";
    public static final String TRANSFER_SUCCESS = "success";
    public static final String TRANSFER_FAILED = "transfer failed. check payment requirements";
    public static final String MISSING_SAVE_VALUES = "JSON should contain source account id, " +
            "destination account id, currency and amount values";

    private final AccountService accountService;
    private final UserService userService;

    @Inject
    public TransferServiceImpl(AccountService accountService, UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }

    @Override
    public List<Transfer> getAllTransfers() {
        return DBStore.getAllTransfers();
    }

    @Override
    public Transfer getTransfer(Long id) {
        Transfer transfer = DBStore.getTransfer(id);
        if (transfer == null) {
            throw new AppException(TRANSFER_NOT_FOUND);
        }
        return transfer;
    }

    @Override
    public Transfer saveTransfer(TransferDTO transfer) {
        checkRequestBody(transfer);
        Transfer saved = makeTransfer(transfer);
        DBStore.addTransfer(saved);
        return saved;
    }

    @Override
    public Transfer deleteTransfer(Long id) {
        Transfer deleted = DBStore.deleteTransfer(id);
        if (deleted == null) {
            throw new AppException(TRANSFER_NOT_FOUND);
        }
        return deleted;
    }

    private Transfer makeTransfer(TransferDTO transfer) {
        Transfer saved;
        Currency transferCurrency = Currency.getInstance(transfer.getCurrency());
        try {
            Account source = accountService.getAccount(transfer.getSourceAccountId());
            Account destination = accountService.getAccount(transfer.getDestinationAccountId());
            // if User is not present, exception is thrown
            userService.getUser(source.getUserId());
            userService.getUser(destination.getUserId());

            Account first = source.getId() < destination.getId() ? source : destination;
            Account second = source.getId() < destination.getId() ? destination : source;

            synchronized (first) {
                synchronized (second) {
                    if (source.getCurrency().equals(transferCurrency) &&
                            destination.getCurrency().equals(transferCurrency) &&
                            transfer.getAmount().compareTo(BigDecimal.ZERO) > 0 &&
                            source.getBalance().compareTo(transfer.getAmount()) >= 0) {

                        accountService.updateAccount(source.getId(), new AccountDTO(null,
                                source.getBalance().subtract(transfer.getAmount()),
                                source.getCurrency().getCurrencyCode()));
                        accountService.updateAccount(destination.getId(), new AccountDTO(null,
                                destination.getBalance().add(transfer.getAmount()),
                                destination.getCurrency().getCurrencyCode()));

                        saved = new Transfer(GENERATED_ID.getAndIncrement(), transfer.getSourceAccountId(),
                                transfer.getDestinationAccountId(), transferCurrency,
                                transfer.getAmount(), Status.EXECUTED, TRANSFER_SUCCESS);
                    } else {
                        throw new AppException(TRANSFER_FAILED);
                    }
                }
            }
        } catch (AppException ex) {
            saved = new Transfer(GENERATED_ID.getAndIncrement(), transfer.getSourceAccountId(),
                    transfer.getDestinationAccountId(), transferCurrency,
                    transfer.getAmount(), Status.FAILED, ex.getMessage());
        }

        return saved;
    }

    private void checkRequestBody(TransferDTO transfer) {
        if (transfer == null) {
            throw new AppException(EMPTY_REQUEST_BODY);
        } else if (transfer.getSourceAccountId() == null || transfer.getDestinationAccountId() == null ||
                transfer.getAmount() == null || transfer.getCurrency() == null) {
            throw new AppException(MISSING_SAVE_VALUES);
        }
    }
}
