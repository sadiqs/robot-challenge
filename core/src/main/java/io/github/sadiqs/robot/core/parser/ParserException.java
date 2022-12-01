package io.github.sadiqs.robot.core.parser;

public class ParserException extends RuntimeException {

    public enum Code {
        INVALID_PLACE_INSTRUCTION_TEXT, INVALID_DIRECTION_INSTRUCTION_TEXT, INVALID_INSTRUCTION_TEXT

    }

    private Code code;

    public ParserException(Code code, String message) {
        super(message);
        this.code = code;
    }

    public Code getCode() {
        return code;
    }
}
