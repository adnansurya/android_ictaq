package elarham.tahfizh.ictaq.Global;

import java.util.UUID;

public class StringUtility {
    public String randomRoomID() {
        String uuid = UUID.randomUUID().toString();
        String roomId = uuid.replaceAll("-","").substring(0,10);
        return "ictaqroom_"+roomId;
    }
}
