package io.github.sadiqs.robot.core.parser;

import io.github.sadiqs.robot.core.instruction.Instruction;
import io.github.sadiqs.robot.core.instruction.Place;
import io.github.sadiqs.robot.core.instruction.Report;
import io.github.sadiqs.robot.core.instruction.RobotInstruction;
import io.github.sadiqs.robot.core.model.Direction;
import io.github.sadiqs.robot.core.model.Position;

public class TextInstructionParser implements InstructionParser<String> {

    public Instruction parse(String instructionText) {

        require(instructionText != null, ParserException.Code.INVALID_INSTRUCTION_TEXT, "Instruction text can not be null");

        var parts = instructionText.trim().split(" ");

        require(parts.length >= 1, ParserException.Code.INVALID_INSTRUCTION_TEXT, "Instruction should not be empty");

        return switch (parts[0]) {
            case "PLACE" -> {
                require(parts.length == 2, ParserException.Code.INVALID_PLACE_INSTRUCTION_TEXT, "PLACE instruction should have one set arguments");
                yield parsePlace(parts[1]);
            }
            case "MOVE" -> RobotInstruction.MOVE;
            case "LEFT" -> RobotInstruction.LEFT;
            case "RIGHT" -> RobotInstruction.RIGHT;
            case "REPORT" -> new Report();
            default ->
                    throw new ParserException(ParserException.Code.INVALID_INSTRUCTION_TEXT, "Unknown instruction: " + instructionText);
        };
    }

    private Place parsePlace(String placePart) {
        var placeArguments = placePart.split(",");

        require(placeArguments.length == 3, ParserException.Code.INVALID_PLACE_INSTRUCTION_TEXT, "PLACE instruction should have three arguments: " + placePart);

        int x = Integer.parseInt(placeArguments[0]);
        int y = Integer.parseInt(placeArguments[1]);

        Direction direction = parseDirection(placeArguments[2]);
        return new Place(new Position(x, y), direction);
    }

    private Direction parseDirection(String directionText) {
        return switch (directionText) {
            case "NORTH" -> Direction.NORTH;
            case "WEST" -> Direction.WEST;
            case "SOUTH" -> Direction.SOUTH;
            case "EAST" -> Direction.EAST;
            default ->
                    throw new ParserException(ParserException.Code.INVALID_DIRECTION_INSTRUCTION_TEXT, "Unknown direction: " + directionText);
        };
    }

    private void require(Boolean condition, ParserException.Code code, String message) {
        if (!condition) {
            throw new ParserException(code, message);
        }
    }

}
