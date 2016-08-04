/*
 * Copyright (c) 2016 LINE Corporation. All rights reserved.
 * LINE Corporation PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package me.keroxp.lib.json.validator

import spock.lang.Specification
import spock.lang.Unroll

import static JsonValidator.expect
import static me.keroxp.lib.json.validator.JsonValueMatchers.isNotNull
import static me.keroxp.lib.json.validator.JsonValueMatchers.isNotNullAndEmpty

class JsonValidatorSpec extends Specification {
    @Unroll
    def "hasProp()"() {
        when:
        expect(json).has(keypath)
        then:
        noExceptionThrown()
        where:
        json                 | keypath
        ["data": 0]          | "data"
        ["data": ""]         | "data"
        ["data": null]       | "data"
        ["data": ["foo": 0]] | "data.foo"
    }

    @Unroll
    def "hasProp() should throw"() {
        when:
        expect(json).has(keypath)
        then:
        thrown(JsonValidationException)
        where:
        json                 | keypath
        new HashMap<>()      | "hoge"
        ["foo": 2]           | "hoge"
        ["data": ["bar": 0]] | "data.foo"
    }

    @Unroll
    def "hasAll()"() {
        when:
        expect(json).hasAll(keypath)
        then:
        noExceptionThrown()
        where:
        json             | keypath
        ["a": 1]         | ["a"]
        ["a": 1, "b": 2] | ["a", "b"]
    }

    @Unroll
    def "hasAll() should throw"() {
        when:
        expect(json).hasAll(keypath)
        then:
        thrown(JsonValidationException)
        where:
        json             | keypath
        new HashMap<>()  | ["a"]
        ["a": 1]         | ["a", "b"]
        ["a": 1, "b": 2] | ["a", "b", "c"]
    }

    @Unroll
    def "hasAtLeast()"() {
        when:
        expect(json).hasAtLeast(cnt, keypath)
        then:
        noExceptionThrown()
        where:
        json                           | keypath              | cnt
        ["a": 1]                       | ["a", "b"]           | 1
        ["data": ["bar": 2], "foo": 1] | ["data.bar", "hoge"] | 1
        ["data": ["bar": 2], "foo": 1] | ["data.bar", "foo"]  | 2
    }

    @Unroll
    def "matchAtLeast()"() {
        when:
        expect(json).matchesAtLeast(cnt, keypath, matcher)
        then:
        noExceptionThrown()
        where:
        json                    | keypath        | matcher           | cnt
        ["foo": "foo"]          | ["foo", "bar"] | isNotNullAndEmpty | 1
        ["bar": "bar",
         "foo": "foo"]          | ["foo", "bar"] | isNotNullAndEmpty | 2
        ["bar": 1, "foo": null] | ["foo", "bar"] | isNotNull         | 1
        ["bar": 1, "foo": 100]  | ["foo", "bar"] | { it > 50 }       | 1
    }

    @Unroll
    def "matchesAll"() {
        when:
        expect(json).matchesAll(keypath, matcher)
        then:
        noExceptionThrown()
        where:
        json                                     | keypath               | matcher
        ["foo": "foo", "bar": "bar", buz: "buz"] | ["foo", "bar", "buz"] | isNotNullAndEmpty
        [a: 0, b: 1, c: 100]                     | ["a", "b", "c"]       | { it > -1 }
    }

    @Unroll
    def "matchesAll - fail"() {
        when:
        expect(json).matchesAll(keypath, matcher)
        then:
        thrown(exp)
        where:
        json                    | keypath         | matcher           | exp
        ["foo": 1]              | ["foo", "bar"]  | isNotNull         | JsonValidationException
        [a: "a", b: "b", c: ""] | ["a", "b", "c"] | isNotNullAndEmpty | JsonValidationException
    }

    @Unroll
    def "has() with type"() {
        expect:
        expect(json).hasType("foo", type)
        where:
        json                | key   | type       | exp
        ["foo": "foo"]      | "foo" | String     | true
        ["foo": 1]          | "foo" | Integer    | true
        ["foo": 1.0]        | "foo" | Double     | true
        ["foo": 1.0]        | "foo" | Integer    | true
        ["foo": []]         | "foo" | Iterable   | true
        ["foo": []]         | "foo" | Collection | true
        ["foo": []]         | "foo" | List       | true
        ["foo": [hoge: ""]] | "foo" | Map        | true
        ["foo": false]      | "foo" | Boolean    | true
    }

    def "matches() with type"() {
        expect:
        expect(["foo": "foo"]).<String> matches("foo", { it == "foo" })
        expect(["foo": ["bar": 1]]).<Integer> matches("foo.bar", { it > 0 })
        expect(["foo": ["bar": true]]).<Integer> matches("foo.bar", { it })
    }

}