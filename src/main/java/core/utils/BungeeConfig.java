package core.utils;

import core.bungee.BungeeCore;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.bukkit.plugin.messaging.Messenger;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class BungeeConfig {

    private Plugin core = BungeeCore.getInstance();
    public static Configuration config;
    public static ConfigurationProvider provider;
    public static File file;

    private String name;

    public BungeeConfig(String name) {
        this.name = name;
        init();
    }

    private void init() {
        this.file = new File(core.getProxy().getPluginsFolder() + "/" + name + ".yml");
        provider = ConfigurationProvider.getProvider(YamlConfiguration.class);
        if (file.exists()) {
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            config = provider.load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getString(String path) {
        return config.getString(path);
    }

    public int getInteger(String path) {
        return config.getInt(path);
    }

    public double getDouble(String path) {
        return config.getDouble(path);
    }

    public float getFloat(String path) {
        return config.getFloat(path);
    }

    public Collection<String> getKeys() {
        return config.getKeys();
    }

    public boolean getBoolan(String path) {
        return config.getBoolean(path);
    }

    public List<?> getList(String path) {
        return config.getList(path);
    }

    public List<String> getStringList(String path) {
        return config.getStringList(path);
    }

    public void set(String path, Object value) {
        config.set(path, value);
        save();
    }

    private void save() {
        try {
            provider.save(config, file);
        } catch (IOException e) {
            core.getLogger().severe("&cCouldn't save a config file with the name " + name);
        }
    }

    public String getName() {
        return name;
    }

}
