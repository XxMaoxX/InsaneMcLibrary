package core.bukkit.event;

import org.bukkit.Bukkit;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.ParameterizedType;

public abstract class Event<T extends org.bukkit.event.Event> {

    public Event(JavaPlugin plugin) {
        this(plugin, EventPriority.NORMAL);
    }

    public Event(JavaPlugin plugin, EventPriority priority) {
        Class<T> clazz = ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        Listener listener = new Listener() {
        };
        Bukkit.getServer().getPluginManager().registerEvent(clazz, listener, priority, ($, rawEvent) -> {
            if (clazz.isAssignableFrom(rawEvent.getClass()))
                execute((T) rawEvent);
        }, plugin);
    }

    public abstract void execute(T event);


}
