package com.saver12.controller;

import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;
import com.anarsoft.vmlens.concurrent.junit.ThreadCount;
import com.saver12.dto.TransferDTO;
import com.saver12.model.Account;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

@RunWith(ConcurrentTestRunner.class)
public class ConcurrentTransfersTest extends AbstractControllerTest {

    @Test
    @ThreadCount(30)
    public void testSaveTransaction() {
        target("/trans")
                .request()
                .post(Entity.entity(
                        new TransferDTO(5L, 2L, "GBP",
                                BigDecimal.valueOf(100)), MediaType.APPLICATION_JSON));
    }

    @After
    public void testCount() {
        Response output = target("/accounts/5").request().get();
        Account account = parseJSON(output, Account.class);
        assertEquals(BigDecimal.ZERO, account.getBalance());
    }
}
