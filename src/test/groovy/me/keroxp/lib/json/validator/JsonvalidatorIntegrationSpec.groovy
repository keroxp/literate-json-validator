package me.keroxp.lib.json.validator

import spock.lang.Specification


class JsonvalidatorIntegrationSpec extends Specification {
    def "has, hasType, matches/ifNot/supply, matches/then/remove"() {
        setup:
        def json = [a: "aaa",
                    b: 0,
                    c: [
                            c1: [
                                    c11: -1,
                                    c12: []
                            ]
                    ]
        ]
        when:
        def res = JsonValidator.modify(json)
                .has("a")
                .hasType("b", Number)
                .<Number> matches("c.c1.c11") { it > 0 }.ifNot().map { it + 10 }
                .<List> matches("c.c1.c12") { it.empty }.then().remove()
                .has("d").ifNot().set("ddd")
                .finish()
        then:
        res == [
                a: "aaa",
                b: 0,
                c: [
                        c1: [
                                c11: 9
                        ]
                ],
                d: "ddd"
        ]
    }
}