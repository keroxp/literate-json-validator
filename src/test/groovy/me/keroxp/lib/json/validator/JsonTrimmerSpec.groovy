package me.keroxp.lib.json.validator

import spock.lang.Specification
import spock.lang.Unroll

import static me.keroxp.lib.json.validator.JsonTrimmer.trimMap


class JsonTrimmerSpec extends Specification {

    @Unroll
    def "trimMap()"() {
        when:
        def json = [
                a: null,
                b: "",
                c: ["", null, 1, [c1: null, c2: "", c3: "hoge"]],
                d: [
                        d1: null,
                        d2: "",
                        d3: "hoge"]
        ]
        def ret = trimMap(json, policy)
        then:
        ret == exp
        where:
        policy                                    | exp
        JsonTrimmer.TrimPolicy.NOT_NULL_NOT_EMPTY | [c: ["", null, 1, [c3: "hoge"]], d: [d3: "hoge"]]
        JsonTrimmer.TrimPolicy.NOT_NULL           | [b: "", c: ["", null, 1, [c2: "", c3: "hoge"]], d: [d2: "", d3: "hoge"]]
        JsonTrimmer.TrimPolicy.NOT_EMPTY          | [a: null, c: ["", null, 1, [c1: null, c3: "hoge"]], d: [d1: null, d3: "hoge"]]
    }

}