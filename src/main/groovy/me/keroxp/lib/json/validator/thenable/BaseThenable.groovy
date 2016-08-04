package me.keroxp.lib.json.validator.thenable

import me.keroxp.lib.json.validator.IJsonValidator
import me.keroxp.lib.json.validator.JsonValidator
import me.keroxp.lib.json.validator.JsonValidatorProxy
import me.keroxp.lib.json.validator.Thenable

import java.util.concurrent.Callable

abstract class BaseThenable<T> extends JsonValidatorProxy implements Thenable<T> {
    BaseThenable(JsonValidator parent) {
        super(parent)
    }

    abstract T successModifier()

    abstract T failureModifier()

    @Override
    T then() {
        resultSet.isSucceeded ? successModifier() : failureModifier()
    }

    @Override
    IJsonValidator then(Callable<Void> callable) {
        if (resultSet.isSucceeded) {
            callable.call()
        }
        return parent
    }

    @Override
    T ifNot() {
        !resultSet.isSucceeded ? successModifier() : failureModifier()
    }

    @Override
    IJsonValidator ifNot(Callable<Void> callable) {
        if (!resultSet.isSucceeded) {
            callable.call()
        }
        return parent
    }
}
