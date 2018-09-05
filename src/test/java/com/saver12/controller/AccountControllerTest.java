package com.saver12.controller;

import com.saver12.dto.AccountDTO;
import com.saver12.exception.AppException;
import com.saver12.model.Account;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static com.saver12.controller.UserControllerTest.JSON_ERROR;
import static com.saver12.service.impl.AccountServiceImpl.*;
import static com.saver12.service.impl.UserServiceImpl.EMPTY_REQUEST_BODY;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class AccountControllerTest extends AbstractControllerTest {

    @Test
    public void testGetAllAccounts() {
        Response output = target("/accounts").request().get();
        List accounts = output.readEntity(List.class);
        assertEquals(200, output.getStatus());
        assertTrue(!accounts.isEmpty());
    }

    @Test
    public void testGetAccount() {
        Response output = target("/accounts/1").request().get();
        Account account = parseJSON(output, Account.class);
        assertEquals(200, output.getStatus());
        assertEquals(BigDecimal.valueOf(400), account.getBalance());
        assertEquals(Currency.getInstance("USD"), account.getCurrency());
    }

    @Test
    public void testGetAccountFail() {
        Response output = target("/accounts/100").request().get();
        AppException error = output.readEntity(AppException.class);
        assertEquals(400, output.getStatus());
        assertEquals(ACCOUNT_NOT_FOUND, error.getMessage());
    }

    @Test
    public void testSaveAccount() {
        Response output = target("/accounts")
                .request()
                .post(Entity.entity(
                        new AccountDTO(1L, BigDecimal.TEN, "AUD"), MediaType.APPLICATION_JSON));
        Account created = parseJSON(output, Account.class);
        assertEquals(201, output.getStatus());
        assertEquals(BigDecimal.TEN, created.getBalance());
        assertEquals(Currency.getInstance("AUD"), created.getCurrency());
    }

    @Test
    public void testSaveAccountFailWithEmptyRequest() {
        Response output = target("/accounts")
                .request()
                .post(Entity.entity("", MediaType.APPLICATION_JSON));
        AppException error = output.readEntity(AppException.class);
        assertEquals(400, output.getStatus());
        assertEquals(EMPTY_REQUEST_BODY, error.getMessage());
    }

    @Test
    public void testSaveAccountFailWithIncorrectJSON() {
        Response output = target("/accounts")
                .request()
                .post(Entity.entity("Not JSON", MediaType.APPLICATION_JSON));
        AppException error = output.readEntity(AppException.class);
        assertEquals(400, output.getStatus());
        assertThat(error.getMessage(), containsString(JSON_ERROR));
    }

    @Test
    public void testSaveAccountFailWithMissingValues() {
        Response output = target("/accounts")
                .request()
                .post(Entity.entity(
                        new AccountDTO(1L, BigDecimal.TEN, null), MediaType.APPLICATION_JSON));
        AppException error = output.readEntity(AppException.class);
        assertEquals(400, output.getStatus());
        assertEquals(MISSING_SAVE_VALUES, error.getMessage());
    }

    @Test
    public void testUpdateAccount() {
        Response output = target("/accounts/2")
                .request()
                .put(Entity.entity(
                        new AccountDTO(null, BigDecimal.ZERO, "GBP"), MediaType.APPLICATION_JSON));
        Account updated = parseJSON(output, Account.class);
        assertEquals(200, output.getStatus());
        assertEquals(BigDecimal.ZERO, updated.getBalance());
    }

    @Test
    public void testUpdateAccountFail() {
        Response output = target("/accounts/22")
                .request()
                .put(Entity.entity(
                        new AccountDTO(null, BigDecimal.TEN, "AUD"), MediaType.APPLICATION_JSON));
        AppException error = output.readEntity(AppException.class);
        assertEquals(400, output.getStatus());
        assertEquals(ACCOUNT_NOT_FOUND, error.getMessage());
    }

    @Test
    public void testUpdateAccountFailWithUserId() {
        Response output = target("/accounts/22")
                .request()
                .put(Entity.entity(
                        new AccountDTO(2L, BigDecimal.TEN, "AUD"), MediaType.APPLICATION_JSON));
        AppException error = output.readEntity(AppException.class);
        assertEquals(400, output.getStatus());
        assertEquals(UPDATE_NO_ID, error.getMessage());
    }

    @Test
    public void testUpdateAccountFailWithEmptyRequest() {
        Response output = target("/accounts/2")
                .request()
                .put(Entity.entity("", MediaType.APPLICATION_JSON));
        AppException error = output.readEntity(AppException.class);
        assertEquals(400, output.getStatus());
        assertEquals(EMPTY_REQUEST_BODY, error.getMessage());
    }

    @Test
    public void testUpdateAccountFailWithIncorrectJSON() {
        Response output = target("/accounts/2")
                .request()
                .put(Entity.entity("Not JSON", MediaType.APPLICATION_JSON));
        AppException error = output.readEntity(AppException.class);
        assertEquals(400, output.getStatus());
        assertThat(error.getMessage(), containsString(JSON_ERROR));
    }

    @Test
    public void testUpdateAccountFailWithMissingValues() {
        Response output = target("/accounts/2")
                .request()
                .put(Entity.entity(
                        new AccountDTO(null, BigDecimal.TEN, null), MediaType.APPLICATION_JSON));
        AppException error = output.readEntity(AppException.class);
        assertEquals(400, output.getStatus());
        assertEquals(MISSING_UPDATE_VALUES, error.getMessage());
    }

    @Test
    public void testDeleteAccount() {
        Response output = target("/accounts/4")
                .request()
                .delete();
        Account deleted = parseJSON(output, Account.class);
        assertEquals(200, output.getStatus());
        assertEquals(BigDecimal.valueOf(350), deleted.getBalance());
        assertEquals(Currency.getInstance("AUD"), deleted.getCurrency());
    }

    @Test
    public void testDeleteAccountFail() {
        Response output = target("/accounts/44")
                .request()
                .delete();
        AppException error = output.readEntity(AppException.class);
        assertEquals(400, output.getStatus());
        assertEquals(ACCOUNT_NOT_FOUND, error.getMessage());
    }
}
