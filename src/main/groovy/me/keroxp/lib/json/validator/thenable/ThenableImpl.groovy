package me.keroxp.lib.json.validator.thenable

import me.keroxp.lib.json.validator.JsonValidator
import me.keroxp.lib.json.validator.PropertyModifier
import me.keroxp.lib.json.validator.modifier.EmptyPropertyModifier
import me.keroxp.lib.json.validator.modifier.PropertyModifierImpl

class ThenableImpl extends BaseThenable<PropertyModifier> {

    private PropertyModifierImpl propertyModifier = new PropertyModifierImpl(parent)
    private EmptyPropertyModifier emptyPropertyModifier = new EmptyPropertyModifier(parent)

    ThenableImpl(JsonValidator parent) {
        super(parent)
    }

    @Override
    PropertyModifier successModifier() {
        return propertyModifier.of(resultSet) as PropertyModifier
    }

    @Override
    PropertyModifier failureModifier() {
        return emptyPropertyModifier.of(resultSet) as PropertyModifier
    }
}
