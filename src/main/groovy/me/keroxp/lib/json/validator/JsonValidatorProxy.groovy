package me.keroxp.lib.json.validator


abstract class JsonValidatorProxy implements IJsonValidator {
    @Delegate
    JsonValidator parent

    protected ResultSet resultSet

    public JsonValidatorProxy of(ResultSet resultSet) {
        this.resultSet = resultSet
        return this
    }

    JsonValidatorProxy(JsonValidator parent) {
        this.parent = parent
    }
}
