package core.bukkit.event;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;

public class Tester  {

    public void test() {
        iEvent event = new iEvent() {
            @Override
            public void execute(Listener listener, Event event) throws EventException {

            }
        };


    }
}
