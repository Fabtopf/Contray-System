package de.Fabtopf.System.API.Enum;

/**
 * Created by Fabi on 24.09.2017.
 */
public enum ServerSetting {

    INFO_Teamspeak("INFO_Teamspeak", "ts.example.com"),
    INFO_Website("INFO_Website", "www.example.com");

    private String path;
    private String defaultMessage;

    private ServerSetting(String path, String defaultMessage) {
        this.path = path;
        this.defaultMessage = defaultMessage;
    }

    public String getPath() {
        return path;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

}
