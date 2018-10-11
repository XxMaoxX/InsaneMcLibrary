package core.exceptions;


import java.util.UUID;

public class UserNotFoundException extends Exception {

    public UserNotFoundException(UUID id, String where) {
        super("Could not find the user with the uuid: { " + id.toString() + " } on " + where );
    }
}
