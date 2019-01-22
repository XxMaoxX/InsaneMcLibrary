package core.bukkit.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.EventExecutor;
import java.util.function.Consumer;


public interface iEvent extends Listener, EventExecutor {

    static <T extends Event> iEvent listen(Plugin plugin, Class<T> type, Consumer<T> listener) {
        return listen(plugin, type, EventPriority.NORMAL, listener);
    }

    static <T extends Event> iEvent listen(Plugin plugin, Class<T> type, EventPriority priority, Consumer<T> listener) {
        final iEvent events = ($, event) -> listener.accept((T) event);
        Bukkit.getPluginManager().registerEvent(type, events, priority, events, plugin);
        return events;
    }

    default void unregister() {
        HandlerList.unregisterAll(this);
    }
}
