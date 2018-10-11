package core.data.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLAcces {

    protected MySQLCredentials mySQLCredentials;
    protected HikariDataSource hikariDataSource;

    protected boolean connected = false;

    protected MySQLAcces(MySQLCredentials mySQLCredentials) {
        this.mySQLCredentials = mySQLCredentials;
    }

    protected void init() {
        final HikariConfig CFG = new HikariConfig();

        CFG.setJdbcUrl(mySQLCredentials.toURL());
        CFG.setUsername(mySQLCredentials.getUsername());
        CFG.setPassword(mySQLCredentials.getPassword());
        CFG.setMaximumPoolSize(10);
        CFG.setMaxLifetime(600000L);
        CFG.setIdleTimeout(300000L);
        CFG.setLeakDetectionThreshold(300000L);
        CFG.setConnectionTimeout(10000L);

        this.hikariDataSource = new HikariDataSource(CFG);
        try {
            if(this.hikariDataSource.getConnection() != null) {
                connected = true;
            }
        } catch (SQLException e) {
            connected = false;
            e.printStackTrace();
        }
    }

    protected void shutdown() {
        connected = false;
        this.hikariDataSource.close();
    }

    public Connection getConnection()  {
        try {
            if(hikariDataSource.getConnection() == null || hikariDataSource.getConnection().isClosed()) {
                System.out.println("Hikari Connection was not found! connecting...");
                init();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            return hikariDataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    protected boolean isConnected() {
        return connected;
    }

    protected MySQLCredentials getMySQLCredentials() {
        return mySQLCredentials;
    }

    protected void setMySQLCredentials(MySQLCredentials mySQLCredentials) {
        this.mySQLCredentials = mySQLCredentials;
    }
}
