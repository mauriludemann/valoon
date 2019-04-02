package valoon.lock;

import org.springframework.stereotype.Service;
import valoon.exceptions.InternalServerException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class LockService {

    private ConcurrentMap<String, String> LOCK_MAP = new ConcurrentHashMap<>();

    public void lock(String key) {
        String value = LOCK_MAP.get(key);
        while (value != null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new InternalServerException("There was an error locking the resource");
            }
            value = LOCK_MAP.get(key);
        }
        LOCK_MAP.put(key, "locked");
    }

    public void unlock(String key) {
        LOCK_MAP.remove(key);
    }
}
