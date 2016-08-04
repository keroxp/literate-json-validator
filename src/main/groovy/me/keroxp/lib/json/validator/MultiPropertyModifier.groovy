package me.keroxp.lib.json.validator

import java.util.function.BiFunction
import java.util.function.Function

interface MultiPropertyModifier extends IJsonValidator {
    public <T> IJsonValidator fill(Function<String, T> function)

    public <T, R> IJsonValidator map(BiFunction<String, T, R> function)

    public IJsonValidator removeUnmatched()
}
