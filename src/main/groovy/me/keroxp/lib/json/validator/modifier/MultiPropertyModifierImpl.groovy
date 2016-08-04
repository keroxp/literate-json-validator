package me.keroxp.lib.json.validator.modifier

import me.keroxp.lib.json.validator.*

import java.util.function.BiFunction
import java.util.function.Function

public class MultiPropertyModifierImpl extends JsonValidatorProxy implements MultiPropertyModifier {

    MultiPropertyModifierImpl(JsonValidator parent) {
        super(parent)
    }

    @Override
    def <T> IJsonValidator fill(Function<String, T> function) {
        resultSet.unmatched.each {
            def v = function.apply(it)
            JsonUtils.setRecursive(parent.json, it, v)
        }
        return parent
    }

    @Override
    def <T, R> IJsonValidator map(BiFunction<String, T, R> function) {
        resultSet.matched.each {
            def v = function.apply(it, JsonUtils.getRecursive(parent.json, it) as T)
            JsonUtils.setRecursive(parent.json, it, v)
        }
        return parent
    }

    @Override
    IJsonValidator removeUnmatched() {
        resultSet.unmatched.each {
            JsonUtils.traverseRecursive(parent.json, it, false) { map, key ->
                map.remove(key)
            }
        }
        return parent
    }
}
