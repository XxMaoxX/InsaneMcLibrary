package core.exceptions;


public class RedisNotConnectedException extends Exception {

    public RedisNotConnectedException( ){
        super("Could not connect to redis, please check the informations you've included!");

    }

}
