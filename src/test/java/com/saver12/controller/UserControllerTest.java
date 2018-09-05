package com.saver12.controller;

import com.saver12.dto.UserDTO;
import com.saver12.exception.AppException;
import com.saver12.model.User;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.saver12.service.impl.UserServiceImpl.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class UserControllerTest extends AbstractControllerTest {

    public static final String JSON_ERROR = "Incomplete data or malformed json";

    @Test
    public void testGetAllUsers() {
        Response output = target("/users").request().get();
        List users = output.readEntity(List.class);
        assertEquals(200, output.getStatus());
        assertTrue(!users.isEmpty());
    }

    @Test
    public void testGetUser() {
        Response output = target("/users/1").request().get();
        User user = output.readEntity(User.class);
        assertEquals(200, output.getStatus());
        assertEquals("John", user.getName());
    }

    @Test
    public void testGetUserFail() {
        Response output = target("/users/100").request().get();
        AppException error = output.readEntity(AppException.class);
        assertEquals(400, output.getStatus());
        assertEquals(USER_NOT_FOUND, error.getMessage());
    }

    @Test
    public void testSaveUser() {
        Response output = target("/users")
                .request()
                .post(Entity.entity(new UserDTO("Connor", "bigguy@mail.org"), MediaType.APPLICATION_JSON));
        User created = output.readEntity(User.class);
        assertEquals(201, output.getStatus());
        assertEquals("Connor", created.getName());
    }

    @Test
    public void testSaveUserFailWithEmptyRequest() {
        Response output = target("/users")
                .request()
                .post(Entity.entity("", MediaType.APPLICATION_JSON));
        AppException error = output.readEntity(AppException.class);
        assertEquals(400, output.getStatus());
        assertEquals(EMPTY_REQUEST_BODY, error.getMessage());
    }

    @Test
    public void testSaveUserFailWithIncorrectJSON() {
        Response output = target("/users")
                .request()
                .post(Entity.entity("Not JSON", MediaType.APPLICATION_JSON));
        AppException error = output.readEntity(AppException.class);
        assertEquals(400, output.getStatus());
        assertThat(error.getMessage(), containsString(JSON_ERROR));
    }

    @Test
    public void testSaveUserFailWithMissingValues() {
        Response output = target("/users")
                .request()
                .post(Entity.entity(new UserDTO("Connor", null), MediaType.APPLICATION_JSON));
        AppException error = output.readEntity(AppException.class);
        assertEquals(400, output.getStatus());
        assertEquals(MISSING_VALUES, error.getMessage());
    }

    @Test
    public void testUpdateUser() {
        Response output = target("/users/2")
                .request()
                .put(Entity.entity(new UserDTO("Terry", "balloon@yahoo.com"), MediaType.APPLICATION_JSON));
        User updated = output.readEntity(User.class);
        assertEquals(200, output.getStatus());
        assertEquals("Terry", updated.getName());
    }

    @Test
    public void testUpdateUserFail() {
        Response output = target("/users/22")
                .request()
                .put(Entity.entity(new UserDTO("Terry", "balloon@yahoo.com"), MediaType.APPLICATION_JSON));
        AppException error = output.readEntity(AppException.class);
        assertEquals(400, output.getStatus());
        assertEquals(USER_NOT_FOUND, error.getMessage());
    }

    @Test
    public void testUpdateUserFailWithEmptyRequest() {
        Response output = target("/users/2")
                .request()
                .put(Entity.entity("", MediaType.APPLICATION_JSON));
        AppException error = output.readEntity(AppException.class);
        assertEquals(400, output.getStatus());
        assertEquals(EMPTY_REQUEST_BODY, error.getMessage());
    }

    @Test
    public void testUpdateUserFailWithIncorrectJSON() {
        Response output = target("/users/2")
                .request()
                .put(Entity.entity("Not JSON", MediaType.APPLICATION_JSON));
        AppException error = output.readEntity(AppException.class);
        assertEquals(400, output.getStatus());
        assertThat(error.getMessage(), containsString(JSON_ERROR));
    }

    @Test
    public void testUpdateUserFailWithMissingValues() {
        Response output = target("/users/2")
                .request()
                .put(Entity.entity(new UserDTO("Terry", null), MediaType.APPLICATION_JSON));
        AppException error = output.readEntity(AppException.class);
        assertEquals(400, output.getStatus());
        assertEquals(MISSING_VALUES, error.getMessage());
    }

    @Test
    public void testDeleteUser() {
        Response output = target("/users/3")
                .request()
                .delete();
        User deleted = output.readEntity(User.class);
        assertEquals(200, output.getStatus());
        assertEquals("David", deleted.getName());
    }

    @Test
    public void testDeleteUserFail() {
        Response output = target("/users/33")
                .request()
                .delete();
        AppException error = output.readEntity(AppException.class);
        assertEquals(400, output.getStatus());
        assertEquals(USER_NOT_FOUND, error.getMessage());
    }
}
