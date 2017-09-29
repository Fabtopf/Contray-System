package de.Fabtopf.System.API.Enum;

/**
 * Created by Fabi on 15.09.2017.
 */
public enum ErrorType {

    MySQL_AlreadyConnected("Database already connected!"),
    MySQL_NotConnected("Database not connected!"),
    MySQL_CouldNotConnect("Could not connect to database!"),
    MySQL_CouldNotDisconnect("Could not close database-connection!"),
    MySQL_UpdateFail("Could not execute update query!"),
    MySQL_ResultFail("Could not get result!");

    private String message;

    private ErrorType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
