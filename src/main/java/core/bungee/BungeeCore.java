package core.bungee;

import net.md_5.bungee.api.plugin.Plugin;

public class BungeeCore extends Plugin {

    private static Plugin instance = new BungeeCore();

    public static Plugin getInstance() {
        return instance;
    }
}
