package io.github.sadiqs.robot.core;

import io.github.sadiqs.robot.core.instruction.RobotInstruction;
import io.github.sadiqs.robot.core.model.Direction;
import io.github.sadiqs.robot.core.model.Position;

public record Robot(Position position, Direction facing) {

    public Robot accept(RobotInstruction command) {
        return switch (command) {
            case LEFT -> left();
            case RIGHT -> right();
            case MOVE -> move();
        };
    }

    /**
     * Alternative implementation for left and right rotations. It which maintains directions in
     * anti-clockwise order and indexes left and right from it to lookup target direction.
     * Can avoid O(n) lookup by maintaining current index as well.
     */

    //    private static final List<Direction> directionRotation = List.of(
    //            Direction.EAST, Direction.NORTH,
    //            Direction.WEST, Direction.SOUTH);
    //
    //    private Robot facingDirection(int offset) {
    //        var newFacing = directionRotation.get((directionRotation.indexOf(facing()) + offset + directionRotation.size()) % directionRotation.size());
    //        return new Robot(position(), newFacing);
    //    }
    private Robot left() {
        var newFacing = switch (facing()) {
            case EAST -> Direction.NORTH;
            case NORTH -> Direction.WEST;
            case WEST -> Direction.SOUTH;
            case SOUTH -> Direction.EAST;
        };
        return new Robot(position(), newFacing);
    }

    private Robot right() {
        var newFacing = switch (facing()) {
            case EAST -> Direction.SOUTH;
            case NORTH -> Direction.EAST;
            case WEST -> Direction.NORTH;
            case SOUTH -> Direction.WEST;
        };
        return new Robot(position(), newFacing);
    }

    private Robot move() {
        var newPosition = switch (facing()) {
            case EAST -> new Position(position().x() + 1, position().y());
            case NORTH -> new Position(position().x(), position().y() + 1);
            case WEST -> new Position(position().x() - 1, position().y());
            case SOUTH -> new Position(position().x(), position().y() - 1);
        };

        return new Robot(newPosition, facing());
    }
}
