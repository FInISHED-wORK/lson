package marco.lson;

import marco.lson.exception.LsonException;
import marco.lson.types.LsonArray;
import marco.lson.types.LsonNumber;
import marco.lson.types.LsonObject;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class LsonTests {

    private final Lson lson = new Lson(new File("./tests/full_test.json"));

    @Test
    void asObjectFail() {
        var json = "[]";
        System.out.println(assertThrows(LsonException.class, () -> new Lson(json).asObject()).getMessage());
    }

    @Test
    void asArrayFail() {
        var json = "{}";
        System.out.println(assertThrows(LsonException.class, () -> new Lson(json).asArray()).getMessage());
    }

    @Test
    void objectKeyIsNull() {
        assertTrue(lson.asObject().isNull("job"));
    }

    @Test
    void objectKeyAsString() {
        assertEquals("Marco", lson.asObject().asString("name"));
    }

    @Test
    void objectHasKey() {
        assertTrue(lson.asObject().hasKey("pets"));
    }

    @Test
    void objectKeyAsArrayFail() {
        System.out.println(assertThrows(LsonException.class, () -> lson.asObject().asArray("age")).getMessage());
    }

    @Test
    void objectKeyAsObjectFail() {
        System.out.println(assertThrows(LsonException.class, () -> lson.asObject().asObject("age")).getMessage());
    }

    @Test
    void objectAsNumber() {
        assertEquals(24d, lson.asObject().asNumber("age"));
    }

    @Test
    void verifyArray() {
        assertEquals(2, lson.asObject().asArray("pets").getValues().size());
        assertTrue(lson.asObject().asArray("pets").get(0) instanceof LsonObject);
        assertTrue(lson.asObject().asArray("pets").get(1) instanceof LsonNumber);
    }

    @Test
    void objectKeyAsBool() {
        assertFalse(lson.asObject().asArray("pets").indexAsObject(0).asBool("calm"));
    }

    @Test
    void objectAsNumberWithExponent() {
        assertEquals(20, lson.asObject().asNumber("strange_number"));
    }

    @Test
    void lsonStringEscapeString() {
        assertEquals("69", lson.asObject().asString("escape_string"));
    }

    @Test
    void objectKeyAsObject() {
        assertEquals(123, lson.asObject().asObject("object").asNumber("ah"));
    }

    @Test
    void constructLsonObject() {
        var lsonObj = new LsonObject();
        lsonObj.add("number", 123);
        lsonObj.add("string", "HI");
        lsonObj.add("bool", false);
        lsonObj.add("array", new LsonArray(new LsonNumber(123)));
        lsonObj.add("obj", new LsonObject());
        assertEquals(123, lsonObj.asNumber("number"));
        assertEquals("HI", lsonObj.asString("string"));
        assertFalse(lsonObj.asBool("bool"));
        assertDoesNotThrow(() -> lsonObj.asArray("array"));
        assertDoesNotThrow(() -> lsonObj.asObject("obj"));
        assertEquals(1, lsonObj.asArray("array").getValues().size());
    }

    @Test
    void arrayIndexAsNumber() {
        assertEquals(1, lson.asObject().asArray("array").indexAsNumber(0));
    }

    @Test
    void arrayIndexAsString() {
        assertEquals("HI", lson.asObject().asArray("array").indexAsString(1));
    }

    @Test
    void arrayIndexAsBool() {
        assertFalse(lson.asObject().asArray("array").indexAsBool(2));
    }

    @Test
    void arrayIndexAsNull() {
        assertTrue(lson.asObject().asArray("array").indexIsNull(3));
    }

    @Test
    void arrayIndexAsObject() {
        assertDoesNotThrow(() -> lson.asObject().asArray("array").indexAsObject(4));
    }

    @Test
    void arrayIndexAsArray() {
        assertDoesNotThrow(() -> lson.asObject().asArray("array").indexAsArray(5));
    }
}
