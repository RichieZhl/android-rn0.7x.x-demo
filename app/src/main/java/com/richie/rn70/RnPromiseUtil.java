package com.richie.rn70;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lylaut on 2022/09/28
 */
public class RnPromiseUtil {
    public static WritableMap convertPromiseSuccessResponseData(int status, String message, Object o) {
        WritableMap map = Arguments.createMap();
        map.putInt("status", status);
        map.putString("message", message);

        if (o == null) {
            map.putNull("data");
        } else if (o instanceof String) {
            map.putString("data", (String) o);
        } else if (o instanceof Integer) {
            map.putInt("data", (Integer) o);
        } else if (o instanceof Boolean) {
            map.putBoolean("data", (Boolean) o);
        } else if (o instanceof Map) {
            map.putMap("data", convertMap((Map<String, Object>) o));
        } else if (o instanceof List) {
            map.putArray("data", convertList((List<Object>) o));
        } else if (o instanceof Set) {
            map.putArray("data", convertSet((Set<Object>) o));
        }
        return map;
    }

    private static WritableMap convertMap(Map<String, Object> da) {
        WritableMap map = Arguments.createMap();

        for (Map.Entry<String, Object> entry : da.entrySet()) {
            Object o = entry.getValue();
            if (o instanceof String) {
                map.putString(entry.getKey(), (String) o);
            } else if (o instanceof Integer) {
                map.putInt(entry.getKey(), (Integer) o);
            } else if (o instanceof Boolean) {
                map.putBoolean(entry.getKey(), (Boolean) o);
            } else if (o instanceof Map) {
                map.putMap(entry.getKey(), convertMap((Map<String, Object>) o));
            } else if (o instanceof List) {
                map.putArray(entry.getKey(), convertList((List<Object>) o));
            } else if (o instanceof Set) {
                map.putArray(entry.getKey(), convertSet((Set<Object>) o));
            }
        }
        return map;
    }

    private static WritableArray convertList(List<Object> da) {
        WritableArray array = Arguments.createArray();
        for (Object o : da) {
            if (o instanceof String) {
                array.pushString((String) o);
            } else if (o instanceof Integer) {
                array.pushInt((Integer) o);
            } else if (o instanceof Boolean) {
                array.pushBoolean((Boolean) o);
            } else if (o instanceof Map) {
                array.pushMap(convertMap((Map<String, Object>) o));
            } else if (o instanceof List) {
                array.pushArray(convertList((List<Object>) o));
            } else if (o instanceof Set) {
                array.pushArray(convertSet((Set<Object>) o));
            }
        }
        return array;
    }

    private static WritableArray convertSet(Set<Object> da) {
        WritableArray array = Arguments.createArray();
        for (Object o : da) {
            if (o instanceof String) {
                array.pushString((String) o);
            } else if (o instanceof Integer) {
                array.pushInt((Integer) o);
            } else if (o instanceof Boolean) {
                array.pushBoolean((Boolean) o);
            } else if (o instanceof Map) {
                array.pushMap(convertMap((Map<String, Object>) o));
            } else if (o instanceof List) {
                array.pushArray(convertList((List<Object>) o));
            } else if (o instanceof Set) {
                array.pushArray(convertSet((Set<Object>) o));
            }
        }
        return array;
    }
}
