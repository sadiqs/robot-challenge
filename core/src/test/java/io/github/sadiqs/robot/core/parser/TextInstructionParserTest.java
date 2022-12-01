package io.github.sadiqs.robot.core.parser;

import io.github.sadiqs.robot.core.instruction.Instruction;
import io.github.sadiqs.robot.core.instruction.Place;
import io.github.sadiqs.robot.core.instruction.Report;
import io.github.sadiqs.robot.core.instruction.RobotInstruction;
import io.github.sadiqs.robot.core.model.Direction;
import io.github.sadiqs.robot.core.model.Position;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;

public class TextInstructionParserTest {

    InstructionParser<String> parser = new TextInstructionParser();

    @ParameterizedTest
    @EnumSource(Direction.class)
    void testParserShouldParseInstructionPlace2(Direction direction) {
        String instructionText = "PLACE 2,3," + direction.name();

        Instruction instruction = parser.parse(instructionText);

        assertThat(instruction).isInstanceOfSatisfying(Place.class, place -> {
            assertThat(place.position()).isEqualTo(new Position(2, 3));
            assertThat(place.facing()).isEqualTo(direction);
        });
    }

    @Test
    void testParserShouldParseInstructionReport() {
        var instructionText = "REPORT";
        Instruction instruction = parser.parse(instructionText);
        assertThat(instruction).isInstanceOf(Report.class);
    }

    @ParameterizedTest
    @EnumSource(RobotInstruction.class)
    void testParserShouldParseRobotInstructions(RobotInstruction instruction) {
        Instruction result = parser.parse(instruction.name());
        assertThat(result).isEqualTo(instruction);
    }


    @Test
    void testParserShouldIgnoreSurroundingSpace() {
        var instructionText = " REPORT     ";
        Instruction instruction = parser.parse(instructionText);
        assertThat(instruction).isInstanceOf(Report.class);
    }

    @Test
    void testParserShouldFailIfUnknownInstruction() {
        var instructionText = " UNK";
        assertThatException().isThrownBy(() -> parser.parse(instructionText))
                .withMessageContaining(instructionText)
                .withMessageContaining("Unknown instruction");
    }


    @Test
    void testParserShouldFailIfPlaceInstructionIncomplete() {
        var instructionTexts = List.of(
                "PLACE",
                "PLACE 2,3",
                "PLACE 3,SOUTH,2",
                "PLACE 3,2,FOO",
                "PLACE 3,SOUTH,2",
                "PLACE 3,SOUTH"
        );

        instructionTexts.forEach(instructionText ->
                assertThatException().isThrownBy(() -> parser.parse(instructionText)));
    }


}