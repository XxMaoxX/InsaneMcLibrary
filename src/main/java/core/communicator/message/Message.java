package core.communicator.message;

public class Message {

    private String type, subType, from , to;
    private String[] arguments;

    public Message(String type, String subType, String from, String to, String[] arguments) {
        this.type = type;
        this.subType = subType;
        this.from = from;
        this.to = to;
        this.arguments = arguments;
    }

    public Message setType(String type) {
        this.type = type;
        return this;
    }
    public Message setSubeType(String subType) {
        this.subType = subType;
        return this;
    }
    public Message setArguments(String... arguments) {
        this.arguments = arguments;
        return this;
    }
    public Message setFrom(String from) {
        this.from = from;
        return this;
    }
    public Message setTo(String to) {
        this.to = to;
        return this;
    }

    public String getType() {
        return type;
    }

    public String getSubType() {
        return subType;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String[] getArguments() {
        return arguments;
    }
}
