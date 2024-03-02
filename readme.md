# lson - JSON parser written in JAVA

Just like JSON but an 'L' at the beginning. <br>
Simple project to learn about parsing text files to a language specification. <br>
<br>
The implementation is fully compatible with the official [JSON Specification](https://www.json.org/json-en.html).

- [x] **Exponents:** 2e+2
- [x] **String Escapes:** "\u0036\u0039"
- [x] **All type:** String, Number, Boolean, Null, Objects, Arrays

### About implementation:

- The numbers are always parsed as doubles.
- The project contains a big test suite of fail and success tests that I got from
  this [repo](https://github.com/nst/JSONTestSuite/tree/master/test_parsing) that is MIT licensed but all not my
  property.
- The actual implementation of an api like GSON is kinda missing.

### Build:

This will build and run all the tests.

```shell
git clone https://github.com/FInISHED-wORK/lson.git
cd lson
gradlew build
```
