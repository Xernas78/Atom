package dev.xernas.atom.string;

import dev.xernas.atom.AtomException;
import dev.xernas.atom.math.NumberTools;

public class StringReader {
    private static final char SYNTAX_ESCAPE = '\\';
    private static final char SYNTAX_DOUBLE_QUOTE = '"';
    private static final char SYNTAX_SINGLE_QUOTE = '\'';

    private final String string;
    private int cursor;

    public StringReader(final StringReader other) {
        this.string = other.string;
        this.cursor = other.cursor;
    }

    public StringReader(final String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public void setCursor(final int cursor) {
        this.cursor = cursor;
    }

    public int getRemainingLength() {
        return string.length() - cursor;
    }

    public int getTotalLength() {
        return string.length();
    }

    public int getCursor() {
        return cursor;
    }

    public String getRead() {
        return string.substring(0, cursor);
    }

    public String getRemaining() {
        return string.substring(cursor);
    }

    public boolean canRead(final int length) {
        return cursor + length <= string.length();
    }

    public boolean canRead() {
        return canRead(1);
    }

    public char peek() {
        return string.charAt(cursor);
    }

    public char peek(final int offset) {
        return string.charAt(cursor + offset);
    }

    public char read() {
        return string.charAt(cursor++);
    }

    public void skip() {
        cursor++;
    }

    public static boolean isAllowedNumber(final char c) {
        return c >= '0' && c <= '9' || c == '.' || c == '-';
    }

    public static boolean isQuotedStringStart(char c) {
        return c == SYNTAX_DOUBLE_QUOTE || c == SYNTAX_SINGLE_QUOTE;
    }

    public void skipWhitespace() {
        while (canRead() && Character.isWhitespace(peek())) {
            skip();
        }
    }

    public boolean isByte() {
        String peeked = peekStringUntil('b', 'B');

        if (peeked == null) {
            return false;
        } else if (peeked.matches("^[0-9]*$")) {
            return NumberTools.isIntSize(peeked);
        }

        return false;
    }

    public boolean isShort() {
        String peeked = peekStringUntil('s', 'S');

        if (peeked == null) {
            return false;
        } else if (peeked.matches("^[0-9]*$")) {
            return NumberTools.isShortSize(peeked);
        }

        return false;
    }

    public boolean isInt() {
        String peeked = peekStringUntil('i', 'I');

        if (peeked == null) {
            peeked = peekRest();
        }

        // Different because peeked can be null, meaning they didn't use the i modifier
        if (peeked.matches("^[0-9]*$")) {
            return NumberTools.isIntSize(peeked);
        }

        return false;
    }

    public boolean isLong() {
        String peeked = peekStringUntil('l', 'L');

        if (peeked == null) {
            return false;
        } else if (peeked.matches("^[0-9]*$")) {
            return NumberTools.isLongSize(peeked);
        }

        return false;
    }

    public boolean isFloat() {
        String peeked = peekStringUntil('f', 'F');

        if (peeked == null) {
            return false;
        }

        return NumberTools.isFloat(peeked);
    }

    public boolean isDouble() {
        String peeked = peekStringUntil('d', 'D');

        if (peeked == null) {
            peeked = peekRest();
        }

        return NumberTools.isDouble(peeked);
    }

    public int readInt() throws AtomException {
        final int start = cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        final String number = string.substring(start, cursor);
        if (number.isEmpty()) {
            throw new AtomException("expected integer was empty");
        }
        try {
            return Integer.parseInt(number);
        } catch (final NumberFormatException ex) {
            cursor = start;
            throw new AtomException(ex);
        }
    }

    public long readLong() throws AtomException {
        final int start = cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        final String number = string.substring(start, cursor);
        if (number.isEmpty()) {
            throw new AtomException("expected long was empty");
        }
        try {
            return Long.parseLong(number);
        } catch (final NumberFormatException ex) {
            cursor = start;
            throw new AtomException(ex);
        }
    }

    public double readDouble() throws AtomException {
        final int start = cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        final String number = string.substring(start, cursor);
        if (number.isEmpty()) {
            throw new AtomException("expected double was empty");
        }
        try {
            return Double.parseDouble(number);
        } catch (final NumberFormatException ex) {
            cursor = start;
            throw new AtomException(ex);
        }
    }

    public float readFloat() throws AtomException {
        final int start = cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        final String number = string.substring(start, cursor);
        if (number.isEmpty()) {
            throw new AtomException("expected float was empty");
        }
        try {
            return Float.parseFloat(number);
        } catch (final NumberFormatException ex) {
            cursor = start;
            throw new AtomException(ex);
        }
    }

    public static boolean isAllowedInUnquotedString(final char c) {
        return c >= '0' && c <= '9'
                || c >= 'A' && c <= 'Z'
                || c >= 'a' && c <= 'z'
                || c == '_' || c == '-'
                || c == '.' || c == '+';
    }

    public String readUnquotedString() {
        final int start = cursor;
        while (canRead() && isAllowedInUnquotedString(peek())) {
            skip();
        }
        return string.substring(start, cursor);
    }

    public String readQuotedString() throws AtomException {
        if (!canRead()) {
            return "";
        }
        final char next = peek();
        if (!isQuotedStringStart(next)) {
            throw new AtomException("Excepted start of quote");
        }
        skip();
        return readStringUntil(next);
    }

    public String peekStringUntil(char... term) {
        StringBuilder result = new StringBuilder();
        for (int x = 0; x < getRemainingLength(); x++) {
            char c = peek(x);
            for (char ch : term) {
                if (c == ch) return result.toString();
            }

            result.append(c);
        }

        return null;
    }

    public String readStringUntil(char terminator) throws AtomException {
        final StringBuilder result = new StringBuilder();
        boolean escaped = false;
        while (canRead()) {
            final char c = read();
            if (escaped) {
                if (c == terminator || c == SYNTAX_ESCAPE) {
                    result.append(c);
                    escaped = false;
                } else {
                    setCursor(getCursor() - 1);
                    throw new AtomException("Invalid escape sequence");
                }
            } else if (c == SYNTAX_ESCAPE) {
                escaped = true;
            } else if (c == terminator) {
                return result.toString();
            } else {
                result.append(c);
            }
        }

        throw new AtomException("Expected end of quote");
    }

    public String peekRest() {
        if (!canRead()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (int x = 0; x < getRemainingLength(); x++) {
            builder.append(peek(x));
        }

        return builder.toString();
    }

    public String readString() throws AtomException {
        if (!canRead()) {
            return "";
        }
        final char next = peek();
        if (isQuotedStringStart(next)) {
            skip();
            return readStringUntil(next);
        }
        return readUnquotedString();
    }

    public boolean readBoolean() throws AtomException {
        final int start = cursor;
        final String value = readString();
        switch (value) {
            case "" -> throw new AtomException("expected boolean was empty");
            case "true" -> {
                return true;
            }
            case "false" -> {
                return false;
            }
            default -> {
                cursor = start;
                throw new AtomException("invalid boolean value");
            }
        }

    }

    public void expect(final char c) throws AtomException {
        if (!canRead() || peek() != c) {
            throw new AtomException("unexpected symbol");
        }
        skip();
    }
}