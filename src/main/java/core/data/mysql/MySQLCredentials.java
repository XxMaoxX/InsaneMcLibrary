package core.data.mysql;

public class MySQLCredentials {

    private String hostname;
    private int port;
    private String username;
    private String password;
    private String database;

    public MySQLCredentials(String hostname, int port, String username, String password, String database) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
    }

    public String toURL() {
        final StringBuilder URL = new StringBuilder();
        URL.append("jdbc:mysql://").append(hostname).append(":").append(port).append("/").append(database);
        return URL.toString();
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }
}
