package io.github.sadiqs.robot.core.command;

import io.github.sadiqs.robot.core.Robot;
import io.github.sadiqs.robot.core.instruction.RobotInstruction;
import io.github.sadiqs.robot.core.model.Table;

import java.util.Optional;

public record RobotCommand(RobotInstruction robotInstruction, Table table,
                           Robot robot) implements Command<Optional<Robot>> {
    @Override
    public Optional<Robot> execute() {
        return Optional.ofNullable(robot).flatMap(robot -> {
            var newRobot = robot.accept(robotInstruction);
            if (table.isOnTheTable(newRobot.position())) {
                return Optional.of(newRobot);
            } else {
                return Optional.empty();
            }
        });
    }
}
