package me.keroxp.lib.json.validator

import java.util.function.Function
import java.util.function.Supplier

interface PropertyModifier extends IJsonValidator {
    public <T> IJsonValidator set(T value)

    public <T> IJsonValidator supply(Supplier<T> supplier)

    public <T, R> IJsonValidator map(Function<T, R> function)

    public IJsonValidator remove()
}