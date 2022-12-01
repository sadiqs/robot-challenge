package io.github.sadiqs.robot.core.instruction;

public sealed interface OperatorInstruction extends Instruction permits Place, Report {
}
