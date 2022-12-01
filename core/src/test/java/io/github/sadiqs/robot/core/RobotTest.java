package io.github.sadiqs.robot.core;

import io.github.sadiqs.robot.core.instruction.RobotInstruction;
import io.github.sadiqs.robot.core.model.Direction;
import io.github.sadiqs.robot.core.model.Position;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Assume the robot is on an infinite 2D plane (well, within the `int` size).
 * Not checking for integer overflow.
 */
class RobotTest {

    @Test
    void testRobotShouldMoveOneStepInCurrentDirection() {
        Robot robot = new Robot(new Position(0, 0), Direction.EAST);
        Robot newRobot = robot.accept(RobotInstruction.MOVE);

        assertThat(newRobot).isNotNull();
        assertThat(newRobot.position()).isEqualTo(new Position(1, 0));
        assertThat(newRobot.facing()).isEqualTo(robot.facing());
    }

    @ParameterizedTest
    @CsvSource({
            "EAST, NORTH",
            "NORTH, WEST",
            "WEST, SOUTH",
            "SOUTH, EAST"
    })
    void testRobotShouldTurnLeftFromAnyDirection(Direction direction, Direction expectedDirection) {
        Robot robot = new Robot(new Position(0, 0), direction);
        Robot newRobot = robot.accept(RobotInstruction.LEFT);

        assertThat(newRobot).isNotNull();
        assertThat(newRobot.position()).isEqualTo(robot.position());
        assertThat(newRobot.facing()).isEqualTo(expectedDirection);
    }

    @ParameterizedTest
    @CsvSource({
            "EAST, SOUTH",
            "NORTH, EAST",
            "WEST, NORTH",
            "SOUTH, WEST"
    })
    void testRobotShouldTurnRightFromAnyDirection(Direction direction, Direction expectedDirection) {
        Robot robot = new Robot(new Position(0, 0), direction);
        Robot newRobot = robot.accept(RobotInstruction.RIGHT);

        assertThat(newRobot).isNotNull();
        assertThat(newRobot.position()).isEqualTo(robot.position());
        assertThat(newRobot.facing()).isEqualTo(expectedDirection);
    }

    @Test
    void testCanRotateAndMove() {
        Robot robot = new Robot(new Position(0, 0), Direction.SOUTH);
        Robot newRobot = robot
                .accept(RobotInstruction.RIGHT)
                .accept(RobotInstruction.MOVE);

        assertThat(newRobot.facing()).isEqualTo(Direction.WEST);
        assertThat(newRobot.position()).isEqualTo(new Position(-1, 0));
    }
}