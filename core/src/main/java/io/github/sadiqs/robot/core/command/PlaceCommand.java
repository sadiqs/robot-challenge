package io.github.sadiqs.robot.core.command;

import io.github.sadiqs.robot.core.Robot;
import io.github.sadiqs.robot.core.instruction.Place;
import io.github.sadiqs.robot.core.model.Table;

import java.util.Optional;

public record PlaceCommand(Place place, Table table, Robot robot) implements Command<Optional<Robot>> {
    @Override
    public Optional<Robot> execute() {
        if (table.isOnTheTable(place.position())) {
            return Optional.of(new Robot(place.position(), place.facing()));
        } else {
            return Optional.empty();
        }
    }
}
