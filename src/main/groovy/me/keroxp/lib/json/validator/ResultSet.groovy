package me.keroxp.lib.json.validator

class ResultSet {
    ResultSet(Iterable<String> keyPaths) {
        this.keyPaths = keyPaths
    }
    Iterable<String> keyPaths = new ArrayList<>()
    Collection<String> matched = new ArrayList<>()
    Collection<String> unmatched = new ArrayList<>()
    boolean isSucceeded = false
}
