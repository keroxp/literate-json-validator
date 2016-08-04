package me.keroxp.lib.json.validator.modifier

import me.keroxp.lib.json.validator.*

import java.util.function.Function
import java.util.function.Supplier

class PropertyModifierImpl extends JsonValidatorProxy implements PropertyModifier {

    PropertyModifierImpl(JsonValidator parent) {
        super(parent)
    }

    private def getKeyPath() {
        return resultSet.keyPaths.first()
    }

    @Override
    def <T> IJsonValidator set(T value) {
        JsonUtils.setRecursive(parent.json, keyPath, value)
        return parent
    }

    @Override
    def <T> IJsonValidator supply(Supplier<T> supplier) {
        JsonUtils.setRecursive(parent.json, keyPath, supplier.get())
        return parent
    }

    @Override
    def <T, R> IJsonValidator map(Function<T, R> function) {
        def v = JsonUtils.getRecursive(parent.json, keyPath)
        JsonUtils.setRecursive(parent.json, keyPath, function.apply(v as T))
        return parent
    }

    @Override
    def IJsonValidator remove() {
        JsonUtils.traverseRecursive(parent.json, keyPath, false, { map, key -> map.remove(key) })
        return parent
    }
}