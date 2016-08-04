package me.keroxp.lib.json.validator

interface IJsonValidator {

    public Map finish()

    public Thenable<PropertyModifier> has(String key) throws JsonValidationException

    public <T> Thenable<PropertyModifier> hasType(String key, Class<T> clazz) throws JsonValidationException

    public <T> Thenable<PropertyModifier> matches(String key, JsonValueMatcher<T> matcher) throws JsonValidationException

    public Thenable<MultiPropertyModifier> hasOneOf(String... keys) throws JsonValidationException

    public Thenable<MultiPropertyModifier> hasOneOf(Iterable<String> keys) throws JsonValidationException

    public <T> Thenable<MultiPropertyModifier> matchesOneOf(String[] keys, JsonValueMatcher<T> matcher) throws JsonValidationException

    public <T> Thenable<MultiPropertyModifier> matchesOneOf(Iterable<String> keys, JsonValueMatcher<T> matcher) throws JsonValidationException

    public Thenable<MultiPropertyModifier> hasAll(String[] keys) throws JsonValidationException

    public Thenable<MultiPropertyModifier> hasAll(Iterable<String> keys) throws JsonValidationException

    public <T> Thenable<MultiPropertyModifier> matchesAll(String[] keys, JsonValueMatcher<T> matcher) throws JsonValidationException

    public <T> Thenable<MultiPropertyModifier> matchesAll(Iterable<String> keys, JsonValueMatcher<T> matcher) throws JsonValidationException

    public Thenable<MultiPropertyModifier> hasAtLeast(int cnt, String... keys) throws JsonValidationException

    public Thenable<MultiPropertyModifier> hasAtLeast(int cnt, Iterable<String> keys) throws JsonValidationException

    public <T> Thenable<MultiPropertyModifier> matchesAtLeast(int cnt, String[] keys, JsonValueMatcher<T> matcher) throws JsonValidationException

    public <T> Thenable<MultiPropertyModifier> matchesAtLeast(int cnt, Iterable<String> keys, JsonValueMatcher<T> matcher) throws JsonValidationException
}
