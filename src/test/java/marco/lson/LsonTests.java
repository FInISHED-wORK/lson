package marco.lson;

import marco.lson.exception.LsonException;
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
        System.out.println(assertThrows(LsonException.class, () -> new Lson(json).asObject()));
    }

    @Test
    void asArrayFail() {
        var json = "{}";
        System.out.println(assertThrows(LsonException.class, () -> new Lson(json).asArray()));
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
    void objectAsArrayFail() {
        System.out.println(assertThrows(LsonException.class, () -> lson.asObject().asArray("age")));
    }

    @Test
    void objectAsNumber() {
        assertEquals(24d, lson.asObject().asNumber("age"));
    }

    @Test
    void verifyArray() {
        assertTrue(lson.asObject().asArray("pets").getValues().size() == 2);
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
}
