package me.keroxp.lib.json.validator

import groovy.json.JsonSlurper

class JsonTrimmer {

    /**
     * A policy for trimming value of a thenMap.
     * This policy will be applied to every single termination value of a thenMap.
     * Termination value is as follows:
     * - String
     * - Number
     * - Boolean
     * - null
     */
    public enum TrimPolicy {
        NOT_NULL_NOT_EMPTY(true, true),
        NOT_NULL(true, false),
        NOT_EMPTY(false, true)
        public final boolean trimNullValue
        public final boolean trimEmptyString

        TrimPolicy(trimNullValue, trimEmptyString) {
            this.trimNullValue = trimNullValue
            this.trimEmptyString = trimEmptyString
        }
    }

    /**
     * Transform json string into a trimmed thenMap.
     * Notice that if a type of top-level object isn't Object,
     * then IllegalArgumentException will be thrown.
     * @param json
     * @param policy
     * @return
     */
    public static Map trimJson(String json, TrimPolicy policy) {
        def slurper = new JsonSlurper()
        def map = slurper.parseText(json)
        if (map instanceof Map) {
            return trimMap(map, policy)
        }
        throw new IllegalArgumentException("parsed json was not object: " + map)
    }

    /**
     * Trimming thenMap values based on the TrimPolicy.
     * All policy-violated values in the thenMap will be removed destructively.
     * Notice that any values in the array won't removed even if it is invalid.
     * @param map
     * @param policy
     * @return
     */
    public static Map trimMap(Map map, TrimPolicy policy) {
        Stack<Map> stack = new Stack<>()
        stack.push(map)
        while (!stack.empty()) {
            Iterator<Map.Entry> itr = stack.pop().entrySet().iterator()
            while (itr.hasNext()) {
                def e = itr.next()
                def v = e.value
                if (v instanceof Map) {
                    stack.push(v)
                } else if (v instanceof Collection) {
                    v.stream().filter { it instanceof Map }.forEach { stack.push(it as Map) }
                } else {
                    def isNull = policy.trimNullValue && v == null
                    def isEmptyString = policy.trimEmptyString && v instanceof String && v.empty
                    if (isNull || isEmptyString) {
                        itr.remove()
                    }
                }
            }
        }
        return map
    }

}
