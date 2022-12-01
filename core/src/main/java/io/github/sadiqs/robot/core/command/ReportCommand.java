package io.github.sadiqs.robot.core.command;

import io.github.sadiqs.robot.core.Robot;

import java.util.Optional;

public record ReportCommand(Robot robot) implements Command<Optional<Robot>> {
    @Override
    public Optional<Robot> execute() {
        return Optional.ofNullable(robot);
    }
}
