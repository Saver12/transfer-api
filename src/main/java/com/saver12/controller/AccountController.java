package com.saver12.controller;

import com.saver12.dto.AccountDTO;
import com.saver12.model.Account;
import com.saver12.service.AccountService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
public class AccountController {

    private final AccountService accountService;

    @Inject
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GET
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GET
    @Path(value = "/{id}")
    public Account getAccount(@PathParam("id") Long id) {
        return accountService.getAccount(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveAccount(AccountDTO account) {
        return Response
                .status(Response.Status.CREATED)
                .entity(accountService.saveAccount(account))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @PUT
    @Path(value = "/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Account updateAccount(@PathParam("id") Long id, AccountDTO account) {
        return accountService.updateAccount(id, account);
    }

    @DELETE
    @Path("/{id}")
    public Account deleteAccount(@PathParam("id") Long id) {
        return accountService.deleteAccount(id);
    }
}
