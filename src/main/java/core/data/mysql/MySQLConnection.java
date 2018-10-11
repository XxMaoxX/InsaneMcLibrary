package core.data.mysql;

import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class MySQLConnection {

    private MySQLAcces mySQLAcces;

    public MySQLConnection() {
    }

    public boolean startConnection(Plugin plugin) {
        if(mySQLAcces == null) {
            this.mySQLAcces = new MySQLAcces(new MySQLInfo(plugin).getMySQLCredentiels(plugin));
            if(!mySQLAcces.isConnected()) {
                mySQLAcces.init();
                return true;
            }
        }

        return false;
    }

    public boolean startConnection(JavaPlugin plugin) {
        if(mySQLAcces == null) {
            this.mySQLAcces = new MySQLAcces(new MySQLInfo(plugin).getMySQLCredentiels(plugin));
            if(!mySQLAcces.isConnected()) {
                mySQLAcces.init();
                return true;
            }
        }

        return false;
    }

    public boolean stopConnection() {
        if(mySQLAcces == null) return false;
        mySQLAcces.shutdown();
        return true;
    }

    public boolean isConnected() {
        if(mySQLAcces == null) return false;
        return mySQLAcces.connected;
    }

    public MySQLAcces getMySQLAcces() {
        return mySQLAcces;
    }
}
