package me.keroxp.lib.json.validator

import org.codehaus.groovy.runtime.typehandling.GroovyCastException

import java.util.function.BiConsumer

class JsonUtils {

    public static Object getRecursive(Map json, String keyPath) {
        def ret = null
        traverseRecursive(json, keyPath, false, { map, key -> ret = map[key] })
        return ret
    }

    public static <T> void setRecursive(Map json, String keyPath, T value) {
        traverseRecursive(json, keyPath, true, { map, key -> map[key] = value })
    }

    public
    static void traverseRecursive(Map json, String keyPath, boolean force, BiConsumer<Map, String> terminator) {
        if (keyPath == null || keyPath.empty) {
            throw new IllegalArgumentException("keypath is null or empty: "+keyPath)
        }
        def tmp = json
        def sum = new StringBuilder()
        def keys = keyPath.split("\\.")
        def ktr = keys.iterator()
        def key = ""
        while (ktr.hasNext()) {
            key = ktr.next()
            sum.append(key)
            if(ktr.hasNext() && tmp == null) {
                throw new IllegalStateException("keyPath remains but ${sum} was null.")
            }
            if (!Map.isAssignableFrom(tmp.getClass())) {
                throw new IllegalStateException(sum + " was not Map but: " + tmp.getClass())
            }
            def map = (tmp as Map)
            if (!map.containsKey(key)) {
                if (force) {
                    map[key] = new LinkedHashMap()
                } else {
                    throw new IllegalStateException("`${sum}` doesn't exist")
                }
            }
            sum.append(".")
            if (!ktr.hasNext()) {
                break
            }
            tmp = map[key]
        }
        terminator.accept(tmp as Map, key)
    }
}
