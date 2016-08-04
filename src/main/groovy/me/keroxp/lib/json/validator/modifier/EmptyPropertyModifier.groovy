package me.keroxp.lib.json.validator.modifier

import me.keroxp.lib.json.validator.IJsonValidator
import me.keroxp.lib.json.validator.JsonValidator
import me.keroxp.lib.json.validator.JsonValidatorProxy
import me.keroxp.lib.json.validator.PropertyModifier

import java.util.function.Function
import java.util.function.Supplier

class EmptyPropertyModifier extends JsonValidatorProxy implements PropertyModifier {

    EmptyPropertyModifier(JsonValidator parent) {
        super(parent)
    }

    @Override
    def <T> IJsonValidator set(T value) {
        return parent
    }

    @Override
    def <T> IJsonValidator supply(Supplier<T> supplier) {
        return parent
    }

    @Override
    def <T, R> IJsonValidator map(Function<T, R> function) {
        return parent
    }

    @Override
    def IJsonValidator remove() {
        return parent
    }
}