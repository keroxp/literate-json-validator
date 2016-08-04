package me.keroxp.lib.json.validator

import spock.lang.Specification
import spock.lang.Unroll


class JsonUtilSpec extends Specification {
    @Unroll
    def "getRecursive"() {
        setup:
        def json = [
                a: "",
                b: [
                        b1: "b1"
                ]
        ]
        expect:
        JsonUtils.getRecursive(json, key) == exp
        where:
        key    | exp
        "a"    | ""
        "b.b1" | "b1"
    }

    @Unroll
    def "setRecursive"() {
        setup:
        def json = [
                a: "",
                b: [
                        b1: "b1"
                ]
        ]
        when:
        JsonUtils.setRecursive(json, key, value)
        matcher(json)
        then:
        noExceptionThrown()
        where:
        key        | value  | matcher
        "a"        | "aaa"  | { JsonValidator.expect(it).matches("a") { it == "aaa" } }
        "b.b1"     | "bbb"  | { JsonValidator.expect(it).matches("b.b1") { it == "bbb" } }
        "b.b2.b11" | "bbbb" | { JsonValidator.expect(it).matches("b.b2.b11") { it == "bbbb" } }
    }

    @Unroll
    def "traverseRecursive - fail"() {
        setup:
        def json = [
                a: "",
                b: [
                        b1: ""
                ]
        ]
        when:
        JsonUtils.traverseRecursive(json, key, false, {map, key -> })
        then:
        thrown(exp)
        where:
        key       | exp
        "a.b"     | IllegalStateException
        "b.b1.bb" | IllegalStateException
        ""        | IllegalArgumentException
        null      | IllegalArgumentException
    }
}