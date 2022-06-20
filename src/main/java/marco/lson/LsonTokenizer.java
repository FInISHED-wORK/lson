package marco.lson;

import marco.lson.exception.LsonParserException;
import marco.lson.rules.*;
import org.apache.commons.text.StringEscapeUtils;

import java.util.*;

public class LsonTokenizer {

    private final String input;
    private final List<Token<?>> tokens = new ArrayList<>();
    private final Map<TokenType, List<Rule>> rules = new HashMap<>();
    private int current;
    private int startOfToken;
    private int line;
    private Scope scope;

    public LsonTokenizer(String input) {
        this.input = input;

        this.rules.put(TokenType.COMMA, Arrays.asList(new NoTrailingComma(), new ExpectedCommaAfterArrayValue()));
        this.rules.put(TokenType.COLON, Arrays.asList(new NoColonAllowedOnArray(), new ExpectKeyBeforeColon()));
        this.rules.put(TokenType.NUMBER, List.of(new CommaAfterObjectValue()));
        this.rules.put(TokenType.BOOLEAN, List.of(new CommaAfterObjectValue()));
        this.rules.put(TokenType.NULL, List.of(new CommaAfterObjectValue()));
        this.rules.put(TokenType.STRING, List.of(new ColonAfterObjectKey(), new CommaAfterObjectValue()));

        this.scan();
    }

    public List<Token<?>> getTokens() {
        return this.tokens;
    }

    public void scan() {
        while (!this.isEOF()) {
            this.tokenize();
            this.startOfToken = this.current;
        }
        this.tokens.add(new Token<>(TokenType.EOF, "EOF", this.startOfToken, this.current, this.line, null, this.scope));
        for (int i = 0; i < this.tokens.size(); i++) {
            Token<?> token = this.tokens.get(i);
            if (this.rules.containsKey(token.type)) {
                var before = i - 1 < 0 ? null : this.tokens.get(i - 1);
                var after = i + 1 > this.tokens.size() ? null : this.tokens.get(i + 1);
                for (Rule rule : this.rules.get(token.type)) {
                    var success = rule.apply(before, token, after);
                    if (!success) {
                        var failMsg = String.format("Token %s in line %s didn't pass rule %s. Message: %s.", token.type, token.line, rule.getClass().getSimpleName(), rule.failMessage());
                        throw new LsonParserException(failMsg);
                    }
                }
            }
        }
    }

    private void tokenize() {
        this.startOfToken = this.current;
        switch (this.getChar(this.current)) {
            case '[' -> {
                this.scope = Scope.ARRAY;
                this.parseSymbol(TokenType.L_BRACKET);
            }
            case ']' -> {
                this.scope = null;
                this.parseSymbol(TokenType.R_BRACKET);
            }
            case '{' -> {
                this.scope = Scope.OBJECT;
                this.parseSymbol(TokenType.L_CURLY);
            }
            case '}' -> {
                this.scope = null;
                this.parseSymbol(TokenType.R_CURLY);
            }
            case ',' -> this.parseSymbol(TokenType.COMMA);
            case ':' -> this.parseSymbol(TokenType.COLON);
            case '"' -> this.parseString();
            case ' ', '\t' -> this.current++;
            case '\n', '\r' -> {
                this.line++;
                this.current++;
            }
            default -> {
                if (Character.isDigit(this.getChar(this.current)) || this.getChar(this.current) == '-')
                    this.parseNumber();
                else if (Character.isAlphabetic(this.getChar(this.current))) this.tryParseKeyword();
                else
                    throw new LsonParserException("Unexpected char " + this.getChar(this.current) + " at " + this.current);
            }
        }
    }

    private void addToken(TokenType token) {
        this.addToken(token, null);
    }

    private <T> void addToken(TokenType tokenType, T value) {
        var lex = this.input.substring(this.startOfToken, this.current);
        this.tokens.add(new Token<>(tokenType, this.startOfToken == this.current ? Character.toString(this.getChar(this.current)) : lex, this.startOfToken, this.current, this.line, value, this.scope));
    }

    private void parseSymbol(TokenType tokenType) {
        this.addToken(tokenType);
        this.current++;
    }

    private void parseString() {
        this.current++;

        var validEscapeChar = Arrays.asList('u', '\\', 'n', 'b', 'f', '/', 'r', 't', '"');

        while (!(this.getChar(this.current) == '"' && this.getChar(this.current - 1) != '\\') && !this.isEOF()) {
            if (this.getChar(this.current) == '\n' || this.getChar(this.current) == '\r') this.line++;
            if (this.getChar(this.current) == '\\' && !validEscapeChar.contains(this.getChar(this.current + 1)) && this.getChar(this.current - 1) != '\\') {
                throw new LsonParserException("Illegal escape sequence " + this.getChar(this.current + 1));
            }
            this.current++;
        }

        if (this.getChar(this.current) != '"') throw new LsonParserException("Found unclosed string.");

        this.current++;
        var value = this.input.substring(this.startOfToken + 1, this.current - 1);

        if (value.contains("\n")) throw new LsonParserException("Found unescaped new line inside the string");
        if (value.contains("\t")) throw new LsonParserException("Found unescaped tab inside the string");
        if (value.contains("\u0000")) throw new LsonParserException("Found unescaped control");

        try {
            value = StringEscapeUtils.unescapeJava(value);
        } catch (Exception e) {
            throw new LsonParserException(e.getMessage());
        }

        this.addToken(TokenType.STRING, value);
    }

    private void parseNumber() {
        while (Character.isDigit(this.getChar(this.current)) || this.getChar(this.current) == 'e' || this.getChar(this.current) == 'E' || this.getChar(this.current) == '+' || this.getChar(this.current) == '.' || this.getChar(this.current) == '-') {
            this.current++;
        }

        var value = this.input.substring(this.startOfToken, this.current).toLowerCase();
        if (this.containsAnyFullWidth(value)) throw new LsonParserException("Contains full width characters.");
        if (value.startsWith("-") && value.length() > 2) {
            if (value.substring(1).startsWith("0") && value.charAt(2) != '.' || value.endsWith(".")) {
                throw new LsonParserException("Invalid number format");
            }
        } else if (value.endsWith(".")) {
            throw new LsonParserException("Invalid number format");
        }

        if (value.contains(".e") || value.contains("-.")) throw new LsonParserException("Invalid number format");

        if (value.length() > 1 && !value.contains(".") && !value.contains("e") && value.startsWith("0"))
            throw new LsonParserException("Invalid number format");

        try {
            this.addToken(TokenType.NUMBER, Double.parseDouble(value));
        } catch (Exception e) {
            throw new LsonParserException(e);
        }
    }

    private void tryParseKeyword() {
        while (Character.isAlphabetic(this.getChar(this.current))) {
            this.current++;
        }
        var value = this.input.substring(this.startOfToken, this.current);
        if (value.equals("false") || value.equals("true")) this.addToken(TokenType.BOOLEAN, Boolean.valueOf(value));
        else if (value.equals("null")) this.addToken(TokenType.NULL);
        else throw new LsonParserException("Expecting 'NULL', 'TRUE', 'FALSE', got '" + value + "'");
    }

    private char getChar(int index) {
        if (this.isEOF() || index >= this.input.length()) return '\0';
        return this.input.charAt(index);
    }

    private boolean isEOF() {
        return this.current >= this.input.length();
    }

    private boolean containsAnyFullWidth(String str) {
        for (char c : str.toCharArray())
            if ((c & 0xff00) == 0xff00) return true;
        return false;
    }

    public enum Scope {
        ARRAY, OBJECT
    }

    public enum TokenType {
        L_BRACKET, R_BRACKET, L_CURLY, R_CURLY, COMMA, COLON, STRING, NUMBER, NULL, EOF, BOOLEAN
    }

    public record Token<T>(TokenType type, String lex, int start, int end, int line, T value, Scope scope) {
    }
}
