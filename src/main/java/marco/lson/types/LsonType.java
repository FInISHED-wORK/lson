package marco.lson.types;

public enum LsonType {
    NULL(null),
    BOOL(LsonBoolean.class),
    NUMBER(LsonNumber.class),
    STRING(LsonString.class),
    OBJECT(LsonObject.class),
    ARRAY(LsonArray.class);

    public Class<?> clazz;

    LsonType(Class<?> clazz) {
        this.clazz = clazz;
    }
}
