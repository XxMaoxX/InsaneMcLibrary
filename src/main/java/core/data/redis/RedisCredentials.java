package core.data.redis;

public class RedisCredentials {

    private String ip;
    private int port;
    private String password;
    private String clientName;

    public RedisCredentials(String ip, int port, String password, String clientName) {
        this.ip = ip;
        this.port = port;
        this.password = password;
        this.clientName = clientName != null || clientName == "" ? clientName : "Redis_bungee_acces";
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String toURL() {
        return "redis://" + ip + ":" + port;
    }

}
