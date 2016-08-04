package me.keroxp.lib.json.validator.modifier

import me.keroxp.lib.json.validator.IJsonValidator
import me.keroxp.lib.json.validator.JsonValidator
import me.keroxp.lib.json.validator.JsonValidatorProxy
import me.keroxp.lib.json.validator.MultiPropertyModifier

import java.util.function.BiFunction
import java.util.function.Function

class EmptyMultiPropertyModifier extends JsonValidatorProxy implements MultiPropertyModifier {

    EmptyMultiPropertyModifier(JsonValidator parent) {
        super(parent)
    }

    @Override
    def <T> IJsonValidator fill(Function<String, T> biFunction) {
        return parent
    }

    @Override
    def <T, R> IJsonValidator map(BiFunction<String, T, R> function) {
        return parent
    }

    @Override
    IJsonValidator removeUnmatched() {
        return parent
    }
}
