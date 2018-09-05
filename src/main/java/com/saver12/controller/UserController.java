package com.saver12.controller;

import com.saver12.dto.UserDTO;
import com.saver12.model.User;
import com.saver12.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    private final UserService userService;

    @Inject
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GET
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GET
    @Path(value = "/{id}")
    public User getUser(@PathParam("id") Long id) {
        return userService.getUser(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveUser(UserDTO user) {
        return Response
                .status(Status.CREATED)
                .entity(userService.saveUser(user))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @PUT
    @Path(value = "/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public User updateUser(@PathParam("id") Long id, UserDTO user) {
        return userService.updateUser(id, user);
    }

    @DELETE
    @Path("/{id}")
    public User deleteUser(@PathParam("id") Long id) {
        return userService.deleteUser(id);
    }
}
