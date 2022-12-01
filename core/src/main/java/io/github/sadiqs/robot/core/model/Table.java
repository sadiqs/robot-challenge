package io.github.sadiqs.robot.core.model;

public record Table(int width, int height) {

    public boolean isOnTheTable(Position position) {
        return position.x() >= 0 && position.y() >= 0 && position.x() < width && position.y() < height;
    }
}
