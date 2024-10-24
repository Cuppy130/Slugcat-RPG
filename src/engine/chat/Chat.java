package engine.chat;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    public static final int history = 8;
    public static List<Message> messages = new ArrayList<>();
    public static boolean open = false;
    public static void push(Message msg){
        messages.removeFirst();
        messages.add(msg);
    }
}
