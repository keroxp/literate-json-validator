package me.keroxp.lib.json.validator

import spock.lang.Specification

import static me.keroxp.lib.json.validator.JsonValueMatchers.isNotNullAndEmpty

class JsonMatherSpec extends Specification {
    def "isNotNullAndEmpty"() {
        expect:
        isNotNullAndEmpty(v) == exp
        where:
        v            | exp
        ""           | false
        null         | false
        "hoge"       | true
        new Object() | false
    }

    def "isNumber"() {

    }
}