package me.keroxp.lib.json.validator

import me.keroxp.lib.json.validator.thenable.MultiThenableImpl
import me.keroxp.lib.json.validator.thenable.ThenableImpl
import groovy.json.JsonSlurper

import java.util.function.Predicate

public class JsonValidator implements IJsonValidator {
    private static final JsonSlurper slurper = new JsonSlurper()
    private static final def alwaysMatches = { true }

    Map json
    boolean throwException
    ThenableImpl thenable = new ThenableImpl(this)
    MultiThenableImpl multiThenable = new MultiThenableImpl(this)

    public static JsonValidator expect(String json) {
        return new JsonValidator(json, true)
    }

    public static JsonValidator expect(Map json) {
        return new JsonValidator(json, true)
    }

    public static JsonValidator modify(String json) {
        return new JsonValidator(json, false)
    }

    public static JsonValidator modify(Map json) {
        return new JsonValidator(json, false)
    }

    public JsonValidator(Map json, boolean throwException) {
        this.json = json
        this.throwException = throwException
    }

    public JsonValidator(String json, boolean throwException) {
        this(slurper.parseText(json) as Map, throwException)
    }

    public JsonValidator throwException(boolean throwException) {
        this.throwException = throwException
        return this
    }

    public JsonValidator trim(JsonTrimmer.TrimPolicy trimPolicy = JsonTrimmer.TrimPolicy.NOT_NULL_NOT_EMPTY) {
        json = JsonTrimmer.trimMap(json, trimPolicy)
        return this
    }

    Map finish() {
        return json
    }

    @Override
    public Thenable has(String key) throws JsonValidationException {
        return thenable.of(resWith(key, _has(key))) as Thenable<PropertyModifier>
    }

    private boolean _has(String key) throws JsonValidationException {
        return _matches(key)
    }

    @Override
    public <T> Thenable hasType(String key, Class<T> clazz) throws JsonValidationException {
        this.<T> matches(key)
    }

    public <T> Thenable matches(String key, JsonValueMatcher<T> matcher = alwaysMatches) throws JsonValidationException {
        return thenable.of(resWith(key, _matches(key, matcher))) as Thenable<PropertyModifier>
    }

    private <T> boolean _matches(String key, JsonValueMatcher<T> matcher = alwaysMatches) throws JsonValidationException{
        try {
            def tmp = json;
            def keys = key.split("\\.")
            def sum = new StringBuilder();
            for (k in keys) {
                sum.append(k)
                if (!(tmp instanceof Map)) {
                    return throwIfPossible("value of `$sum` is not Object but ${tmp.getClass()}")
                } else if (!(tmp as Map).containsKey(k)) {
                    return throwIfPossible("json must contain `$sum` key")
                }
                sum.append(".")
                tmp = tmp[k]
            }
            if (tmp != null && !T.isAssignableFrom(tmp.getClass())) {
                return throwIfPossible("expected type of `${key}` differs with actual type: ${tmp.getClass().simpleName}")
            }
            if (!matcher.matches(tmp as T)) {
                return throwIfPossible("`$key` doesn't match expected value: " + tmp)
            }
        } catch (JsonValidationException e) {
            throw e
        } catch (Exception e) {
            throw new RuntimeException(e)
        }
        return true
    }

    private static ResultSet resWith(String key, boolean success) {
        def res = new ResultSet([key])
        res.keyPaths << key
        success ? res.matched << key : res.unmatched << key
        res.isSucceeded = success
        return res
    }

    private boolean throwIfPossible(String e) {
        if (throwException) {
            throw new JsonValidationException(e)
        }
        return false
    }

    public Thenable<MultiPropertyModifier> hasOneOf(String... keys) throws JsonValidationException {
        hasAtLeast 1, keys.toList()
    }

    public Thenable<MultiPropertyModifier> hasOneOf(Iterable<String> keys) throws JsonValidationException {
        hasAtLeast 1, keys
    }

    public <T> Thenable<MultiPropertyModifier> matchesOneOf(String[] keys, JsonValueMatcher<T> matcher) throws JsonValidationException {
        matchesAtLeast 1, keys.toList(), matcher
    }

    public <T> Thenable<MultiPropertyModifier> matchesOneOf(Iterable<String> keys, JsonValueMatcher<T> matcher) throws JsonValidationException {
        matchesAtLeast 1, keys, matcher
    }

    public Thenable<MultiPropertyModifier> hasAll(String[] keys) throws JsonValidationException {
        hasAtLeast keys.size(), keys.toList()
    }

    public Thenable<MultiPropertyModifier> hasAll(Iterable<String> keys) throws JsonValidationException {
        hasAtLeast keys.size(), keys
    }

    public <T> Thenable<MultiPropertyModifier> matchesAll(String[] keys, JsonValueMatcher<T> matcher) throws JsonValidationException {
        matchesAtLeast keys.size(), keys.toList(), matcher
    }

    public <T> Thenable<MultiPropertyModifier> matchesAll(Iterable<String> keys, JsonValueMatcher<T> matcher) throws JsonValidationException {
        matchesAtLeast keys.size(), keys, matcher
    }

    public Thenable<MultiPropertyModifier> hasAtLeast(int cnt, String... keys) throws JsonValidationException {
        atLeast cnt, keys.toList(), { has(it) }
    }

    public Thenable<MultiPropertyModifier> hasAtLeast(int cnt, Iterable<String> keys) throws JsonValidationException {
        atLeast cnt, keys, { _has(it) }
    }

    public <T> Thenable<MultiPropertyModifier> matchesAtLeast(int cnt, String[] keys, JsonValueMatcher<T> matcher) throws JsonValidationException {
        atLeast cnt, keys.toList(), { _matches(it, matcher) }
    }

    public <T> Thenable<MultiPropertyModifier> matchesAtLeast(int cnt, Iterable<String> keys, JsonValueMatcher<T> matcher) throws JsonValidationException {
        atLeast cnt, keys, { _matches(it, matcher) }
    }

    private Thenable<MultiPropertyModifier> atLeast(int cnt, Iterable<String> keys, Predicate<String> closure) throws JsonValidationException {
        def res = new ResultSet(keys)
        for (k in keys) {
            try {
                if (closure.test(k)){
                    res.matched << k
                } else {
                    res.unmatched << k
                }
            } catch (Exception e) {
                res.unmatched << k
            }
        }
        res.isSucceeded = cnt <= res.matched.size()
        if (!res.isSucceeded) {
            if (throwException) {
                throw new JsonValidationException("json must contain at least ${cnt} values of [${keys.join(",")}], but contains ${res.unmatched.size()} values")
            }
        }
        return multiThenable.of(res) as Thenable<MultiPropertyModifier>
    }

}