package me.keroxp.lib.json.validator

import java.util.concurrent.Callable

interface Thenable<T> extends IJsonValidator {
    public T then()

    public IJsonValidator then(Callable<Void> callable)

    public T ifNot()

    public IJsonValidator ifNot(Callable<Void> callable)
}