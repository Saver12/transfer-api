package com.saver12.controller;

import com.saver12.dto.TransferDTO;
import com.saver12.model.Transfer;
import com.saver12.service.TransferService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/trans")
@Produces(MediaType.APPLICATION_JSON)
public class TransferController {

    private final TransferService transferService;

    @Inject
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @GET
    public List<Transfer> getAllTransfers() {
        return transferService.getAllTransfers();
    }

    @GET
    @Path(value = "/{id}")
    public Transfer getTransfer(@PathParam("id") Long id) {
        return transferService.getTransfer(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveTransaction(TransferDTO transfer) {
        return Response
                .status(Response.Status.CREATED)
                .entity(transferService.saveTransfer(transfer))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @DELETE
    @Path("/{id}")
    public Transfer deleteTransfer(@PathParam("id") Long id) {
        return transferService.deleteTransfer(id);
    }
}
