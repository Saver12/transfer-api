package com.saver12.controller;

import com.saver12.dto.TransferDTO;
import com.saver12.exception.AppException;
import com.saver12.model.Transfer;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static com.saver12.controller.UserControllerTest.JSON_ERROR;
import static com.saver12.model.Transfer.Status;
import static com.saver12.service.impl.AccountServiceImpl.ACCOUNT_NOT_FOUND;
import static com.saver12.service.impl.TransferServiceImpl.*;
import static com.saver12.service.impl.UserServiceImpl.EMPTY_REQUEST_BODY;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class TransferControllerTest extends AbstractControllerTest {

    @Test
    public void testGetAllTransfers() {
        Response output = target("/trans").request().get();
        List transfers = output.readEntity(List.class);
        assertEquals(200, output.getStatus());
        assertTrue(!transfers.isEmpty());
    }

    @Test
    public void testGetTransfer() {
        Response output = target("/trans/1").request().get();
        Transfer transfer = parseJSON(output, Transfer.class);
        assertEquals(200, output.getStatus());
        assertEquals(BigDecimal.valueOf(100), transfer.getAmount());
        assertEquals(Currency.getInstance("USD"), transfer.getCurrency());
        assertEquals(Status.EXECUTED, transfer.getStatus());
    }

    @Test
    public void testGetTransferFail() {
        Response output = target("/trans/100").request().get();
        AppException error = output.readEntity(AppException.class);
        assertEquals(400, output.getStatus());
        assertEquals(TRANSFER_NOT_FOUND, error.getMessage());
    }

    @Test
    public void testSaveTransaction() {
        Response output = target("/trans")
                .request()
                .post(Entity.entity(
                        new TransferDTO(1L, 3L, "USD",
                                BigDecimal.valueOf(100)), MediaType.APPLICATION_JSON));
        Transfer created = parseJSON(output, Transfer.class);
        assertEquals(201, output.getStatus());
        assertEquals(BigDecimal.valueOf(100), created.getAmount());
        assertEquals(Currency.getInstance("USD"), created.getCurrency());
        assertEquals(Status.EXECUTED, created.getStatus());
    }

    @Test
    public void testSaveTransferFailWithMissingAccount() {
        Response output = target("/trans")
                .request()
                .post(Entity.entity(
                        new TransferDTO(1L, 133L, "USD",
                                BigDecimal.valueOf(100)), MediaType.APPLICATION_JSON));
        Transfer created = parseJSON(output, Transfer.class);
        assertEquals(201, output.getStatus());
        assertEquals(ACCOUNT_NOT_FOUND, created.getMessage());
    }

    @Test
    public void testSaveTransferFailWithZeroAmount() {
        Response output = target("/trans")
                .request()
                .post(Entity.entity(
                        new TransferDTO(1L, 3L, "USD", BigDecimal.ZERO),
                        MediaType.APPLICATION_JSON));
        Transfer created = parseJSON(output, Transfer.class);
        assertEquals(201, output.getStatus());
        assertEquals(TRANSFER_FAILED, created.getMessage());
    }

    @Test
    public void testSaveTransferFailWithDifferentCurrency() {
        Response output = target("/trans")
                .request()
                .post(Entity.entity(
                        new TransferDTO(1L, 2L, "USD",
                                BigDecimal.valueOf(100)), MediaType.APPLICATION_JSON));
        Transfer created = parseJSON(output, Transfer.class);
        assertEquals(201, output.getStatus());
        assertEquals(TRANSFER_FAILED, created.getMessage());
    }

    @Test
    public void testSaveTransferFailWithInsufficientFund() {
        Response output = target("/trans")
                .request()
                .post(Entity.entity(
                        new TransferDTO(1L, 3L, "USD",
                                BigDecimal.valueOf(1500)), MediaType.APPLICATION_JSON));
        Transfer created = parseJSON(output, Transfer.class);
        assertEquals(201, output.getStatus());
        assertEquals(TRANSFER_FAILED, created.getMessage());
    }

    @Test
    public void testSaveTransferFailWithEmptyRequest() {
        Response output = target("/trans")
                .request()
                .post(Entity.entity("", MediaType.APPLICATION_JSON));
        AppException error = output.readEntity(AppException.class);
        assertEquals(400, output.getStatus());
        assertEquals(EMPTY_REQUEST_BODY, error.getMessage());
    }

    @Test
    public void testSaveTransferFailWithIncorrectJSON() {
        Response output = target("/trans")
                .request()
                .post(Entity.entity("Not JSON", MediaType.APPLICATION_JSON));
        AppException error = output.readEntity(AppException.class);
        assertEquals(400, output.getStatus());
        assertThat(error.getMessage(), containsString(JSON_ERROR));
    }

    @Test
    public void testSaveTransferFailWithMissingValues() {
        Response output = target("/trans")
                .request()
                .post(Entity.entity(
                        new TransferDTO(null, 3L, "USD", null),
                        MediaType.APPLICATION_JSON));
        AppException error = output.readEntity(AppException.class);
        assertEquals(400, output.getStatus());
        assertEquals(MISSING_SAVE_VALUES, error.getMessage());
    }

    @Test
    public void testDeleteTransfer() {
        Response output = target("/trans/3")
                .request()
                .delete();
        Transfer deleted = parseJSON(output, Transfer.class);
        assertEquals(200, output.getStatus());
        assertEquals(BigDecimal.valueOf(50), deleted.getAmount());
        assertEquals(Currency.getInstance("GBP"), deleted.getCurrency());
    }

    @Test
    public void testDeleteTransferFail() {
        Response output = target("/trans/133")
                .request()
                .delete();
        AppException error = output.readEntity(AppException.class);
        assertEquals(400, output.getStatus());
        assertEquals(TRANSFER_NOT_FOUND, error.getMessage());
    }
}
