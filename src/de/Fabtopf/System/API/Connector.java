package de.Fabtopf.System.API;

import com.google.common.base.Function;
import de.Fabtopf.System.API.Enum.ErrorType;
import de.Fabtopf.System.Utilities.Cache;
import de.Fabtopf.System.Utilities.Functions;
import de.Fabtopf.System.Utilities.Main;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;

/**
 * Created by Fabi on 15.09.2017.
 */
public class Connector {

    private String host;
    private String database;
    private String username;
    private String password;

    private int port;

    private Connection con;

    public Connector(String host, int port, String database, String username, String password) {
        this.port = port;
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public void connect() {
        if(!isConnected()) {
            try {
                con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
                return;
            } catch(SQLException e) {
                if(Cache.devmode) e.printStackTrace();
                Messager.sendError(ErrorType.MySQL_CouldNotConnect);
                Cache.shutdown = true;
                Functions.shutdownPlugin();
                return;
            }
        } else {
            Messager.sendError(ErrorType.MySQL_AlreadyConnected);
            return;
        }
    }

    public void disconnect() {
        if(isConnected()) {
            try {
                con.close();
                con = null;
                return;
            } catch(SQLException e) {
                if(Cache.devmode) e.printStackTrace();
                Messager.sendError(ErrorType.MySQL_CouldNotDisconnect);
                return;
            }
        } else {
            Messager.sendError(ErrorType.MySQL_NotConnected);
            return;
        }
    }

    public void reconnect() {
        if(isConnected()) disconnect();
        connect();
    }

    public boolean isConnected() { return con != null; }

    public void update(String qry) {
        new BukkitRunnable() {
            @Override
            public void run() {

                if(!isConnected()) connect();
                if(!isConnected()) {
                    Cache.shutdown = true;
                    Functions.shutdownPlugin();
                    return;
                }

                try {
                    Statement st = con.createStatement();
                    st.execute(qry);
                    st.close();
                    return;
                } catch(SQLException e) {
                    if(Cache.devmode) e.printStackTrace();
                    Messager.sendError(ErrorType.MySQL_UpdateFail);
                    return;
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    public ResultSet getResult(String qry) {

        if(!isConnected()) connect();
        if(!isConnected()) {
            Cache.shutdown = true;
            Functions.shutdownPlugin();
            return null;
        }

        try {
            return con.createStatement().executeQuery(qry);
        } catch(SQLException e) {
            if(Cache.devmode) e.printStackTrace();
            Messager.sendError(ErrorType.MySQL_ResultFail);
            return null;
        }

    }

    public boolean tableExists(String table) {
        try {
            return getResult("SHOW TABLES LIKE '" + table + "'").next();
        } catch(SQLException e) {
            if(Cache.devmode) e.printStackTrace();
            Messager.sendError(ErrorType.MySQL_ResultFail);
            return false;
        }
    }

    public boolean getBoolean(String table, String checkColumn, Object checkValue, String getColumn) {

        try {
            ResultSet rs = getResult("SELECT * FROM " + table);

            while(rs.next()) {
                if(rs.getObject(checkColumn).toString().equalsIgnoreCase(checkValue.toString())) {
                    return rs.getBoolean(getColumn);
                }
            }
        } catch(SQLException e) {
            if(Cache.devmode) e.printStackTrace();
            Messager.sendError(ErrorType.MySQL_ResultFail);
            return false;
        }

        return false;

    }

    public String getString(String table, String checkColumn, Object checkValue, String getColumn) {

        try {
            ResultSet rs = getResult("SELECT * FROM " + table);

            while(rs.next()) {
                if(rs.getObject(checkColumn).toString().equalsIgnoreCase(checkValue.toString())) {
                    return rs.getString(getColumn);
                }
            }
        } catch(SQLException e) {
            if(Cache.devmode) e.printStackTrace();
            Messager.sendError(ErrorType.MySQL_ResultFail);
            return null;
        }

        return null;

    }

    public int getInt(String table, String checkColumn, Object checkValue, String getColumn) {

        try {
            ResultSet rs = getResult("SELECT * FROM " + table);

            while(rs.next()) {
                if(rs.getObject(checkColumn).toString().equalsIgnoreCase(checkValue.toString())) {
                    return rs.getInt(getColumn);
                }
            }
        } catch(SQLException e) {
            if(Cache.devmode) e.printStackTrace();
            Messager.sendError(ErrorType.MySQL_ResultFail);
            return -1;
        }

        return -1;

    }

    public long getLong(String table, String checkColumn, Object checkValue, String getColumn) {

        try {
            ResultSet rs = getResult("SELECT * FROM " + table);

            while(rs.next()) {
                if(rs.getObject(checkColumn).toString().equalsIgnoreCase(checkValue.toString())) {
                    return rs.getLong(getColumn);
                }
            }
        } catch(SQLException e) {
            if(Cache.devmode) e.printStackTrace();
            Messager.sendError(ErrorType.MySQL_ResultFail);
            return -1;
        }

        return -1;

    }

    public double getDouble(String table, String checkColumn, Object checkValue, String getColumn) {

        try {
            ResultSet rs = getResult("SELECT * FROM " + table);

            while(rs.next()) {
                if(rs.getObject(checkColumn).toString().equalsIgnoreCase(checkValue.toString())) {
                    return rs.getDouble(getColumn);
                }
            }
        } catch(SQLException e) {
            if(Cache.devmode) e.printStackTrace();
            Messager.sendError(ErrorType.MySQL_ResultFail);
            return -1;
        }

        return -1;

    }

    public Object getObject(String table, String checkColumn, Object checkValue, String getColumn) {

        try {
            ResultSet rs = getResult("SELECT * FROM " + table);

            while(rs.next()) {
                if(rs.getObject(checkColumn).toString().equalsIgnoreCase(checkValue.toString())) {
                    return rs.getObject(getColumn);
                }
            }
        } catch(SQLException e) {
            if(Cache.devmode) e.printStackTrace();
            Messager.sendError(ErrorType.MySQL_ResultFail);
            return null;
        }

        return null;

    }

}
