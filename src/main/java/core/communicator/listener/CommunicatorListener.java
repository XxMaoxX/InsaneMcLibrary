package core.communicator.listener;

import core.communicator.message.Message;

public abstract class CommunicatorListener {

    private final String channel;

    public CommunicatorListener(String channel) {
        this.channel = channel;
    }

    public abstract void execute(Message message);

    public String getChannel() {
        return channel;
    }
}
