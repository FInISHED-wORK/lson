package marco.lson;

import marco.lson.LsonTokenizer.Token;
import marco.lson.LsonTokenizer.TokenType;
import marco.lson.exception.LsonParserException;
import marco.lson.types.*;

import java.util.List;

public class LsonParser {

    private final String input;
    private final List<Token<?>> tokens;

    private int index = 0;
    private int maxRecursion = 5000;
    private int recursionCount = 0;

    private ILsonValue rootJson;

    public LsonParser(String input) {
        this.input = input;
        this.tokens = new LsonTokenizer(input).getTokens();
    }

    public ILsonValue parse() {
        if (this.getCurrent().type() == TokenType.L_BRACKET) {
            var lsonArray = this.parseArray();
            if (this.getCurrent().type() != TokenType.EOF) throw new LsonParserException("Expecting 'EOF'");
            return lsonArray;
        } else if (this.getCurrent().type() == TokenType.L_CURLY) {
            var lsonObject = this.parseObject();
            if (this.getCurrent().type() != TokenType.EOF) throw new LsonParserException("Expecting 'EOF'");
            return lsonObject;
        } else if (this.tokens.size() == 1 && this.tokens.get(0).type() == TokenType.EOF) {
            throw new LsonParserException("Empty file.");
        } else {
            if (this.tokens.size() > 2) throw new LsonParserException("Expecting only value and EOF.");
            if (this.tokens.get(0).type() != TokenType.STRING && this.tokens.get(0).type() != TokenType.NUMBER
                    && this.tokens.get(0).type() != TokenType.NULL && this.tokens.get(0).type() != TokenType.BOOLEAN)
                throw new LsonParserException("Only allowed STRING, NUMBER, NULL, BOOLEAN if the root isn't a object or array. Got " + this.getCurrent().type());
            return this.parseSingleType();
        }
    }

    private ILsonValue parseSingleType() {
        switch (this.getCurrent().type()) {
            case NUMBER -> {
                return new LsonNumber(((Double) this.getCurrent().value()));
            }
            case STRING -> {
                return new LsonString(((String) this.getCurrent().value()));
            }
            case BOOLEAN -> {
                return new LsonBoolean(((boolean) this.getCurrent().value()));
            }
            default -> {
                return null;
            }
        }
    }

    private LsonObject parseObject() {
        var object = new LsonObject();
        this.index++;
        this.recursionCount++;
        if (this.recursionCount >= this.maxRecursion) throw new LsonParserException("Recursion limit!");
        var key = "";
        while (this.getCurrent().type() != TokenType.R_CURLY) {
            switch (this.getCurrent().type()) {
                case STRING -> {
                    if (this.getNext().type() == TokenType.COLON) {
                        key = ((String) this.getCurrent().value());
                    } else {
                        object.add(key, new LsonString(((String) this.getCurrent().value())));
                    }
                    this.index++;
                }
                case NUMBER -> {
                    object.add(key, new LsonNumber(((Double) this.getCurrent().value())));
                    this.index++;
                }
                case L_BRACKET -> {
                    this.recursionCount++;
                    object.add(key, this.parseArray());
                }
                case L_CURLY -> {
                    this.recursionCount++;
                    object.add(key, this.parseObject());
                }
                case BOOLEAN -> {
                    object.add(key, ((boolean) this.getCurrent().value()));
                    this.index++;
                }
                case NULL -> {
                    object.add(key, (ILsonValue) null);
                    this.index++;
                }
                case COLON, COMMA, R_CURLY, R_BRACKET -> this.index++;
                default -> {
                    throw new LsonParserException("Expecting 'EOF', '}', ',', ']', got '" + this.getCurrent().lex() + "'");
                }
            }
        }
        this.recursionCount--;
        this.index++;
        return object;
    }

    private LsonArray parseArray() {
        var array = new LsonArray();
        this.index++;
        this.recursionCount++;
        if (this.recursionCount >= this.maxRecursion) throw new LsonParserException("Recursion limit!");
        while (this.getCurrent().type() != TokenType.R_BRACKET) {
            switch (this.getCurrent().type()) {
                case STRING -> {
                    array.add(new LsonString(((String) this.tokens.get(this.index).value())));
                    this.index++;
                }
                case NUMBER -> {
                    array.add(new LsonNumber(((Double) this.tokens.get(this.index).value())));
                    this.advanceIfNextIsValid(TokenType.COMMA, TokenType.R_BRACKET);
                }
                case L_BRACKET -> {
                    this.recursionCount++;
                    array.add(this.parseArray());
                }
                case L_CURLY -> {
                    this.recursionCount++;
                    array.add(this.parseObject());
                }
                case BOOLEAN -> {
                    array.add(new LsonBoolean(((boolean) this.getCurrent().value())));
                    this.index++;
                }
                case NULL -> {
                    array.add(null);
                    this.index++;
                }
                case R_CURLY, R_BRACKET -> this.index++;
                case COMMA -> {
                    this.index++;
                }
                case COLON -> throw new LsonParserException("Expecting 'EOF', '}', ',', ']', got ':'");
                default -> {
                    throw new LsonParserException(this.getCurrent());
                }
            }
        }
        this.index++;
        this.recursionCount--;
        return array;
    }

    public Token<?> getCurrent() {
        return this.tokens.get(this.index);
    }

    public Token<?> getNext() {
        return this.tokens.get(this.index + 1);
    }

    public void advanceIfNextIsValid(TokenType... type) {
        for (TokenType tokenType : type) {
            if (getNext().type() == tokenType) {
                this.index++;
                return;
            }
        }
        var builder = new StringBuilder().append("Expecting ");
        for (TokenType tokenType1 : type) {
            builder.append("'").append(tokenType1).append("', ");
        }
        builder.append("got '").append(this.getNext().lex()).append("'");
        throw new LsonParserException(builder.toString());
    }

}
