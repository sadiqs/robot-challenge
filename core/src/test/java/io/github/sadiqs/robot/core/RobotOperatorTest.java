package io.github.sadiqs.robot.core;

import io.github.sadiqs.robot.core.instruction.Instruction;
import io.github.sadiqs.robot.core.instruction.Place;
import io.github.sadiqs.robot.core.instruction.Report;
import io.github.sadiqs.robot.core.instruction.RobotInstruction;
import io.github.sadiqs.robot.core.model.Direction;
import io.github.sadiqs.robot.core.model.Position;
import io.github.sadiqs.robot.core.model.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


class RobotOperatorTest {

    Place placeAtOrigin = new Place(new Position(0, 0), Direction.EAST);
    Report reportPosition = new Report();

    @BeforeEach
    void setUp() {
    }

    @Test
    void testShouldNotPlaceRobotOutsideOfTable() {

        var operator = new RobotOperator(new Table(2, 2));
        var placeOutsideOfTable = new Place(new Position(10, 10), Direction.NORTH);

        Stream<Instruction> instructions = Stream.of(
                placeOutsideOfTable,
                reportPosition
        );

        var reports = operator.processInstructions(instructions).count();

        assertThat(reports).isEqualTo(0);
    }

    @Test
    void testShouldIgnoreInstructionsIfRobotNotPlaced() {

        Stream<Instruction> instructions = Stream.of(
                RobotInstruction.MOVE,
                RobotInstruction.LEFT,
                RobotInstruction.MOVE,
                reportPosition,
                RobotInstruction.MOVE,
                RobotInstruction.LEFT,
                reportPosition
        );

        var operator = new RobotOperator(new Table(5, 5));

        var reportsCount = operator.processInstructions(instructions).count();

        assertThat(reportsCount).isEqualTo(0);
    }

    @Test
    void testShouldApplyInstructionsAfterRobotPlaced() {
        var operator = new RobotOperator(new Table(5, 5));

        Stream<Instruction> instructions = Stream.of(
                RobotInstruction.MOVE,
                RobotInstruction.LEFT,
                reportPosition,
                placeAtOrigin,
                RobotInstruction.MOVE,
                RobotInstruction.MOVE,
                RobotInstruction.LEFT,
                reportPosition
        );


        var reports = operator.processInstructions(instructions).toList();

        assertThat(reports.size()).isEqualTo(1);

        var robot = reports.get(0);

        assertThat(robot.position().x()).isEqualTo(2);
        assertThat(robot.position().y()).isEqualTo(0);
        assertThat(robot.facing()).isEqualTo(Direction.NORTH);
    }

    @Test
    void testShouldIgnoreInstructionsThatCauseFallDown() {

        var operator = new RobotOperator(new Table(2, 2));

        Stream<Instruction> instructions = Stream.of(
                placeAtOrigin,
                RobotInstruction.MOVE,
                RobotInstruction.MOVE,
                RobotInstruction.MOVE,
                RobotInstruction.MOVE,
                RobotInstruction.MOVE,
                RobotInstruction.LEFT,
                RobotInstruction.MOVE,
                RobotInstruction.MOVE,
                RobotInstruction.MOVE,
                RobotInstruction.MOVE,
                RobotInstruction.RIGHT,
                RobotInstruction.RIGHT,
                reportPosition
        );


        var reports = operator.processInstructions(instructions).toList();

        assertThat(reports.size()).isEqualTo(1);

        var robot = reports.get(0);

        assertThat(robot.position().x()).isEqualTo(1);
        assertThat(robot.position().y()).isEqualTo(1);
        assertThat(robot.facing()).isEqualTo(Direction.SOUTH);
    }

    @Test
    void testShouldReportAsManyTimesAsRequestedAfterPlacing() {

        var operator = new RobotOperator(new Table(2, 2));

        Stream<Instruction> instructions = Stream.of(
                reportPosition,
                RobotInstruction.MOVE,
                RobotInstruction.MOVE,
                RobotInstruction.RIGHT,
                RobotInstruction.MOVE,
                RobotInstruction.MOVE,
                reportPosition,
                RobotInstruction.LEFT,
                placeAtOrigin,
                RobotInstruction.MOVE,
                RobotInstruction.MOVE,
                reportPosition,
                RobotInstruction.MOVE,
                RobotInstruction.MOVE,
                RobotInstruction.RIGHT,
                reportPosition
        );


        var reports = operator.processInstructions(instructions).toList();

        assertThat(reports.size()).isEqualTo(2);
    }

}