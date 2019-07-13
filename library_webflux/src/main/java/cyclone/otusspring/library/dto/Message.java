package cyclone.otusspring.library.dto;

import lombok.Data;

/**
 * May be used for sending status messages to user interface. Currently not used.
 */
@Data
public class Message {
    private final String text;
    private final Type type;

    public Message(String text) {
        this.text = text;
        this.type = Type.INFO;
    }

    public Message(String text, Type type) {
        this.text = text;
        this.type = type;
    }

    public enum Type {
        INFO,
        ERROR
    }
}
