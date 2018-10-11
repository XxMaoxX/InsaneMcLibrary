package core.data.mysql;


import core.utils.BukkitConfig;
import core.utils.BungeeConfig;
import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class MySQLInfo {

    public MySQLInfo(Plugin plugin) {
        BungeeConfig config = new BungeeConfig("settings");
    }

    public MySQLInfo(JavaPlugin plugin) {
        BukkitConfig config = new BukkitConfig(plugin.getDataFolder().getAbsolutePath(), "settings");
    }

    protected MySQLCredentials getMySQLCredentiels(Plugin plugin) {
        BungeeConfig settings = new BungeeConfig("settings");
        if (settings.getKeys().isEmpty()) {
            settings.set("mysql.username", "");
            settings.set("mysql.password", "");
            settings.set("mysql.database", "");
            settings.set("mysql.hostname", "");
            settings.set("mysql.port", "");
            return null;
        }

        String username = settings.getString("mysql.username");
        String password = settings.getString("mysql.password");
        String database = settings.getString("mysql.database");
        String host = settings.getString("mysql.hostname");
        int port = settings.getInteger("mysql.port");

        return new MySQLCredentials(host, port, username, password, database);
    }

    protected MySQLCredentials getMySQLCredentiels(JavaPlugin plugin) {
        BukkitConfig settings = new BukkitConfig(plugin.getDataFolder().getAbsolutePath(), "settings");
        if (settings.getKeys(false).isEmpty()) {
            settings.set("mysql.username", "");
            settings.set("mysql.password", "");
            settings.set("mysql.database", "");
            settings.set("mysql.hostname", "");
            settings.set("mysql.port", "");
            return null;
        }

        String username = settings.getString("mysql.username");
        String password = settings.getString("mysql.password");
        String database = settings.getString("mysql.database");
        String host = settings.getString("mysql.hostname");
        int port = settings.getInt("mysql.port");

        return new MySQLCredentials(host, port, username, password, database);
    }

}
