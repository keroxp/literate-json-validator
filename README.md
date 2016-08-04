# json-validator
A literate runtime json validator for java, groovy.

## Usage

### Groovy
```groovy
String json = ...
try {
    JsonValidator.expect(json)
        .has("user.name")
        .hasType("user.friends", Collection)
        .matches("user.id", JsonValueMatchers.isNotNullAndEmpty)
        .<Integer>matches("user.age", { it > -1 }) 
        .hasOneOf(["user.data.foo", "user.data.bar"])
        .<String>matchesOneOf(["user.data.buz", "user.data.var"], { it == "foo" })
} catch (JsonValidationException e) {
    // handle error
}
```

### Java

```java
String json = ...;
try {
    JsonValidator.expect(json)
        .has("user.friends")
        .matches("user.id", JsonValueMatchers.isNotNullAndEmpty)
        .<Integer>matches("user.age", (it) -> it > -1 ) 
        .hasOneOf(Arrays.asList("user.data.foo", "user.data.bar"))
        .<String>matchesOneOf(Arrays.asList("user.data.buz", "user.data.var"), (it) -> it == "foo" });
} catch (JsonValidationException e) {
    // handle error
}
```

## Validators

###has

Check whether json has a field for the keypath.
This validator does not check what type of value was set.

```groovy
expect(json).has("path.to.value")
```

###hasType

Check if json has a typed value for the keypath.
If value is null, then an exception will be thrown. 
#### Groovy
```groovy
expect(json).hasType("path.to.number", Number)
```

#### Java
```groovy
expect(json).hasType("path.to.number", Number.getClass())
```

###hasOneOf

Check whether json has a one of values for the keypaths.
If json does not have at least one value, then an exception will be thrown. 
 ```groovy
 expect(json).hasOneOf("path.to.value", "path.to.value2", "path.to.value2")
 ```

###hasAll

```groovy
expect(json).hasAll("path.to.value", "path.to.value2", "path.to.value2")
```

###hasAtLeast

```groovy
expect(json).hasAtLeast(2, "path.to.value", "path.to.value2", "path.to.value2")
```

###matches

Check whether the value for keypath matches the matcher. If json does not have a value or value does not match, then an exception will be thrown.

```groovy
expect(json).matches("path.to.value", JsonValueMatchers.isNotNullAndEmpty)
```

Type parameter also can be used to define a type of value.
Type-Checking will be done before matcher called.

```groovy
expect(json).<Number>matches("path.to.number") { it > 0 }
```

Function reference also can be passed as a matcher.
```java
expect(json).<List>matches("path.to.list", List::isEmpty)
```

###matchesOneOf

```groovy
expect(json).matchesOneOf(["a", "b", "c"], JsonValueMatchers.isNotNullAndEmpty)
```

###matchesAtLeast

```groovy
expect(json).matchesAtLeast(2, ["a", "b", "c"], JsonValueMatchers.isNotNullAndEmpty)
```

###matchesAll

```groovy
expect(json).matchesAll(["a", "b", "c"], JsonValueMatchers.isNotNullAndEmpty)
```

## Modifying Json

`JsonValidator` enable you to modify json at runtime with literate syntax.
 To start modifying json, use `modify` factory instead of `expect`. In modifying context, no exception will be thrown even if validation rule is violated
. A given json object will be modified destructively.
### Groovy

```groovy
String json = ...
JsonValidator.modify(json)
    .has("user.name").ifNot().set("default name")  
    .<Integer>matches("user.age") { it < 0 }.then().set(0)
    .hasType("user.friends", Colllection).ifNot().set([])   
    .finish()
    // => [user: [ name: "default name", age: 0, friends: [] ]
```

## Thenable chain

All returned value of validation methods can be chained by `then()` or `ifNot()`.
The former will only be called if validation result is true, the latter is false.

```groovy
def json = [a: "aaa"]
expect(json).has("a").then { println(it) }
// => "aaa"
```
```groovy
def json = [a: 1]
modify(json)
    .matches("a"){ it > 0 }.then().map { it*100 }
    .has("b").ifNot().set("bbb")    
    .finish()    
// => [a: 100, b: "bbb"]
```

## PropertyModifiers

is chainable after

- has
- hasType
- matches

## set

```
modify(json).has("user").ifNot().set([name: "user", age: 0])
```

## supply

```groovy
modify(json).has("user).ifNot().supply { // supply function here }
```

## map

```groovy
modify(json).matches("user.name") { it.length > 20 }.then().map { it[0..19] }
```

## remove

```groovy
modify(json).has("unexpectedField").then().remove()
```

## MultiPropertyModifiers

is chainable after

- hasOneOf
- hasAtLeast
- hasAll
- matchesOneOf
- matchesAtLeast
- matchesAll

### fill

Fills unmatched keypaths with the value.
The destination value will be set recursively, inserting a new Object
```groovy
def json = [a: "aaa"]
modify(json).hasAll("a","b","c").ifNot().fill { "${it}"*3 }.finish()
 // => [a: "aaa", b: "bbb", c: "ccc"]
```

```groovy
def json = [a: "aa"]
modify(json).hasAll("a", "b.b1.b11").ifNot().fill {
    switch(it) {
        case "b.b1.b11": "b11"
        default: ""
    }
}.finish()
// => [a: "aa, b: [b1: [b11: "b11]]]
```

### map

Map all matched values.

```groovy
def json = [a: 1, b: -1, c: 10]
modify(json)
    .matchesOneOf(["a", "b", "c"]) { it > 0 }.then().map { keyPath ,v -> "${keyPath}-mapped-${v}" }
    .finish()
// => [a: "a-mapped-1", b: -1, c: "c-mapped-10"]    
```

### removeUnmatched()

Remove all unmatched fields.
This operation won't affect a structure of the nested object even if object after removing is empty. 

```groovy
def json = [a: "aaa", b: null, c: [c1: ""]]
modify(json)
    .matchesAll(["a", "b", "c.c1"], isNotNullAndEmpty)
    .ifNot()
    .removeUnmatched()
    .finish()
//  => [a: "aaa"]    
```

# List key path support

Currently We don't support list key path like [JsonPath](https://github.com/jayway/JsonPath)
The reason is that this library was developed to validate request json from a client.

# Contributor

- Yusuke Sakruai
