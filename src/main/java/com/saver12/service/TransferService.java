package com.saver12.service;

import com.saver12.dto.TransferDTO;
import com.saver12.model.Transfer;

import java.util.List;

public interface TransferService {

    List<Transfer> getAllTransfers();

    Transfer getTransfer(Long id);

    Transfer saveTransfer(TransferDTO transfer);

    Transfer deleteTransfer(Long id);
}
