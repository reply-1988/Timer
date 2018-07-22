package reply.timer;

public class MessageEvent {

    public final String message;
    public final int position;
    public final int type;

    public MessageEvent(String message, int position, int type) {
        this.message = message;
        this.position = position;
        this.type = type;
    }
}
