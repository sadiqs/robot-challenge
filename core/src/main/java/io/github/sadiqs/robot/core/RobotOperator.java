package io.github.sadiqs.robot.core;

import io.github.sadiqs.robot.core.command.Command;
import io.github.sadiqs.robot.core.command.PlaceCommand;
import io.github.sadiqs.robot.core.command.ReportCommand;
import io.github.sadiqs.robot.core.command.RobotCommand;
import io.github.sadiqs.robot.core.instruction.Instruction;
import io.github.sadiqs.robot.core.instruction.Place;
import io.github.sadiqs.robot.core.instruction.Report;
import io.github.sadiqs.robot.core.instruction.RobotInstruction;
import io.github.sadiqs.robot.core.model.Table;

import java.util.Optional;
import java.util.stream.Stream;

public final class RobotOperator implements InstructionProcessor<Instruction, Robot> {

    private final Table table;

    public RobotOperator(Table table) {
        this.table = table;
    }

    public Stream<Robot> processInstructions(Stream<Instruction> instructionStream) {

        var operator = new RobotOperatorImpl();

        return instructionStream.map(operator::translateInstruction).flatMap(command ->
                command.execute().stream()
                        .map(newRobot -> operator.robot = newRobot)
                        .filter(robot -> command instanceof ReportCommand));
    }

    class RobotOperatorImpl {

        private Robot robot;

        private Command<Optional<Robot>> translateInstruction(Instruction instruction) {
            if (instruction instanceof Report) {
                return new ReportCommand(robot);
            } else if (instruction instanceof Place place) {
                return new PlaceCommand(place, table, robot);
            } else if (instruction instanceof RobotInstruction robotInstruction) {
                return new RobotCommand(robotInstruction, table, robot);
            } else {
                throw new RuntimeException("Unsupported instruction: " + instruction);
            }
        }
    }

}