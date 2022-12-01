package io.github.sadiqs.robot.core.instruction;

import io.github.sadiqs.robot.core.model.Direction;
import io.github.sadiqs.robot.core.model.Position;

public record Place(Position position, Direction facing) implements OperatorInstruction {
}
