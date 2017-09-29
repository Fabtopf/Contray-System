package de.Fabtopf.System.API.Enum;

/**
 * Created by Fabi on 15.09.2017.
 */
public enum MessageType {

    System_PluginEnabled("SYSTEM_PluginEnabled", "&8[&c%PLUGIN_NAME%&8] &7> &aDas Plugin wurde aktiviert!"),
    System_PluginDisabled("SYSTEM_PluginDisabled", "&8[&c%PLUGIN_NAME%&8] &7> &cDas Plugin wurde deaktiviert!"),
    System_PluginNotConfigured("SYSTEM_PluginNotConfigured", "&8[&c%PLUGIN_NAME%&8] &7> &cDas Plugin wurde noch nicht konfiguriert!"),
    System_PluginShutdown("SYSTEM_PluginShutdown", "&8[&c%PLUGIN_NAME%&8] &7> &cDas Plugin wurde aufgrund von Fehlern deaktiviert!"),
    System_Allowed("SYSTEM_Allowed", "erlaubt"),
    System_Denied("System_Denied", "verboten"),

    Command_NoPerm("COMMAND_NoPerm", "&8[&c%PLUGIN_NAME%&8] &cDu hast keine Berechtigung, diesen Befehl zu nutzen!"),
    Command_Disabled("COMMAND_Disabled", "&8[&c%PLUGIN_NAME%&8] &cDieser Befehl ist im Moment deaktiviert!"),
    Command_ShowUsage("COMMAND_ShowUsage", "&8[&c%PLUGIN_NAME%&8] &4Usage: &c%COMMAND_USAGE%"),

    Database_PlayerDoesntExist("DATABASE_PlayerDoesntExist", "&8[&c%PLUGIN_NAME%&8] &cDieser Spieler ist nicht in der Datenbank registriert!"),

    BanModule_AlreadyBanned("BANMODULE_AlradyBanned", "&8[&c%PLUGIN_NAME%&8] &cDieser Spieler ist bereits gebannt!"),
    BanModule_NotBanned("BANMODULE_NotBanned", "&8[&c%PLUGIN_NAME%&8] &cDieser Spieler ist nicht gebannt!"),
    BanModule_SuccessfullyBanned("BANMODULE_SuccessfullyBanned", "&8[&c%PLUGIN_NAME%&8] &cDer Spieler &e%BANNED_PLAYER% &cwurde erfolgreich gebannt!"),
    BanModule_SuccessfullyUnbanned("BANMODULE_SuccessfullyUnbanned", "&8[&c%PLUGIN_NAME%&8] &cDer Spieler &e%UNBANNED_PLAYER% &cwurde erfolgreich entbannt!"),
    BanModule_BanScreen("BANMODULE_BanScreen", "&8- %PREFIX% &8-\n\n&cDu wurdest &9%TIMETYPE% &cvom Server gebannt!\n&cVerbleibende Zeit: &7%OUTSTANDING_TIME%\n\n&cGrund: &7%REASON%\n\n&cEntbannungsantrag stellbar: &7%UNBAN_APPLY%\n\n&eWebsite: &7%WEBSITE%\n&eTeamSpeak: &7%TEAMSPEAK%"),
    BanModule_BanScreen_TimeStamp("BANMODULE_BanScreen_TimeStamp", "%DAYS% %DAYS_LONG% %HOURS% %HOURS_LONG% %MINUTES% %MINUTES_LONG% %SECONDS% %SECONDS_LONG%"),
    BanModule_InvaliedTimeStamp_1("BANMODULE_InvaliedTimeStamp_1", "&cDu darfst jede Zeiteinheit nur maximal 1 mal nutzen!"),
    BanModule_InvaliedTimeStamp_2("BANMODULE_InvaliedTimeSpamp_2", "&cIn der Zeitangabe dürfen nur Zahlen oder die Buchstaben s, m, h oder d enthalten sein!"),
    BanModule_InvaliedTimeStamp_3("BANMODULE_InvaliedTimeSpamp_3", "&cBitte nutze eine gültige Zeitangabe!"),

    TimeManager_Seconds_long("TIMEMANAGER_Seconds_long", "Sekunden"),
    TimeManager_Minutes_long("TIMEMANAGER_Minutes_long", "Minuten"),
    TimeManager_Hours_long("TIMEMANAGER_Hours_long", "Stunden"),
    TimeManager_Days_long("TIMEMANAGER_Days_long", "Tage"),
    TimeManager_Seconds_short("TIMEMANAGER_Seconds_short", "s"),
    TimeManager_Minutes_short("TIMEMANAGER_Minutes_short", "m"),
    TimeManager_Hours_short("TIMEMANAGER_Hours_short", "h"),
    TimeManager_Days_short("TIMENANAGER_Days_short", "d"),
    TimeManager_temp("TIMEMANAGER_temp", "temporär"),
    TimeManager_perm("TIMEMANAGER_perm", "permanent");

    private String path;
    private String defaultMessage;

    private MessageType(String path, String defaultMessage) {
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
