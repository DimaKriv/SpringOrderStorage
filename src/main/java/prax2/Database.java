package prax2;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public final class Database {

    private static Database activeDatabase = null;
    private AtomicInteger id;
    private Map<Integer, String> jsonStringData;

    private Database() {
        id = new AtomicInteger(0);
        jsonStringData = new HashMap<>();
    }

    public int createNewId() {
        return id.addAndGet(1);
    }

    public void saveStringJson(int id, String jsonString) {
        jsonStringData.put(id, jsonString);
    }

    public String getJsonString(int id) {
       return jsonStringData.getOrDefault(id, null);
    }

    public static Database init() {
        if (activeDatabase == null) {
            activeDatabase = new Database();
        }
        return activeDatabase;
    }
}
