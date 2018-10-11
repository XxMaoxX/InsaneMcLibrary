package core.exceptions;


public class MySqlNotConnectedException extends Exception {

    public MySqlNotConnectedException() {
        super("Could not connect to mysql database! please check the informations you've included! ");
    }

}
