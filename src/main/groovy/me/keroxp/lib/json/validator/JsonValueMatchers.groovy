package me.keroxp.lib.json.validator


final class JsonValueMatchers {

    public static final def isNotNull = { it != null }
    public static final def isNotNullAndEmpty = { it != null && it instanceof String && !it.empty }
    public static final def isList = { it != null && Collection.isAssignableFrom(it.class) }
    public static final def isObject = { it != null && Map.isAssignableFrom(it.class) }
    public static final def isNumber = { it != null && Number.isAssignableFrom(it.class) }
    public static final def isBoolean = { it != null && Boolean.isAssignableFrom(it.class) }

}
