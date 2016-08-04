package me.keroxp.lib.json.validator

import spock.lang.Specification

import static me.keroxp.lib.json.validator.JsonValidator.expect
import static me.keroxp.lib.json.validator.JsonValidator.modify
import static me.keroxp.lib.json.validator.JsonValueMatchers.isNotNullAndEmpty

class PropertyModifierSpec extends Specification {
    def "then{}"() {
        when:
        def ok = false
        modify(["foo": ""]).matches("foo", isNotNullAndEmpty).ifNot { ok = true }
        then:
        ok
        when:
        modify(["foo": null]).matches("foo") { it == null }.then { ok = true }
        then:
        ok
    }

    def "map"() {
        when:
        modify(["foo": "foo"])
                .hasType("foo", String).then().map { "hello " + it }
                .matches("foo") { it == "hello foo" }
        then:
        noExceptionThrown()
    }

    def "set"() {
        when:
        def json = ["foo": ""]
        modify(json).matches("foo", isNotNullAndEmpty).ifNot().supply { "foo" }
        then:
        json["foo"] == "foo"
        when:
        json = ["foo": ""]
        expect(json).matches("foo") { it == "" }.then().set("hoo")
        then:
        json["foo"] == "hoo"
    }

    def "remove"() {
        when:
        def json = ["foo": null]
        expect(json).throwException(false).matches("foo", isNotNullAndEmpty).ifNot().remove()
        then:
        !json.containsKey("foo")
        when:
        json = ["foo": 1]
        expect(json).throwException(false).hasType("foo", Number).then().remove()
        then:
        !json.containsKey("foo")
    }
}