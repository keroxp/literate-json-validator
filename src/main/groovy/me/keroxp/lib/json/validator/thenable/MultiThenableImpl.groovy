package me.keroxp.lib.json.validator.thenable

import me.keroxp.lib.json.validator.JsonValidator
import me.keroxp.lib.json.validator.MultiPropertyModifier
import me.keroxp.lib.json.validator.modifier.EmptyMultiPropertyModifier
import me.keroxp.lib.json.validator.modifier.MultiPropertyModifierImpl

class MultiThenableImpl extends BaseThenable<MultiPropertyModifier> {

    private MultiPropertyModifierImpl multiPropertyModifier = new MultiPropertyModifierImpl(parent)
    private EmptyMultiPropertyModifier emptyMultiPropertyModifier = new EmptyMultiPropertyModifier(parent)

    MultiThenableImpl(JsonValidator parent) {
        super(parent)
    }

    @Override
    MultiPropertyModifier successModifier() {
        return multiPropertyModifier.of(resultSet) as MultiPropertyModifier
    }

    @Override
    MultiPropertyModifier failureModifier() {
        return emptyMultiPropertyModifier.of(resultSet) as MultiPropertyModifier
    }
}
