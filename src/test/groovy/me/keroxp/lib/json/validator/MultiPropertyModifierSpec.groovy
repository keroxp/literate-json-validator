package me.keroxp.lib.json.validator

import spock.lang.Specification


class MultiPropertyModifierSpec extends Specification {
    def "fill"() {
        when:
        def json = [
                a: "a",
                b: null,
                bb: false,
                bbb: null
        ]
        def res = JsonValidator.modify(json)
            .hasOneOf(["a", "aa"]).then().fill { "${it}-filled".toString() }
            .matchesAll(["b", "bb", "bbb"], JsonValueMatchers.isNotNull).ifNot().fill { "${it}-filled".toString() }
            .finish()
        then:
        res == [
                a: "a",
                aa: "aa-filled",
                b: "b-filled",
                bb: false,
                bbb: "bbb-filled"
        ]
    }

    def "map"() {
        when:
        def json = [
                a: "a",
                b: 1
        ]
        def res = JsonValidator.modify(json)
            .hasOneOf("a", "aa").then().<String, String>map { key, v -> v + "-mapped" }
            .matchesOneOf(["b", "bb"]) { it > 0 }.then().<Number, String> map { key, v -> "${v+10}" }
            .finish()
        then:
        res == [
                a: "a-mapped",
                b: "11"
        ]
    }

    def "remove"() {
        when:
        def json = [
                a: "aa",
                b: 1,
                c: []
        ]
        def res = JsonValidator.modify(json)
            .matchesAll(["a", "b", "c"], { it instanceof String }).ifNot().removeUnmatched()
            .finish()
        then:
        res == [
                a: "aa"
        ]
    }
}