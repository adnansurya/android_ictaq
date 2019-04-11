package elarham.tahfizh.ictaq.Global;

import java.util.UUID;

public class StringUtility {
    public String randomID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-","").substring(0,11);
    }
}
