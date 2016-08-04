package me.keroxp.lib.json.validator

@FunctionalInterface
interface JsonValueMatcher<T> {
    boolean matches(T tgt)
}