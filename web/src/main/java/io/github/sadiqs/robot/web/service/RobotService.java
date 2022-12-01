package io.github.sadiqs.robot.web.service;

import io.github.sadiqs.robot.core.InstructionProcessor;
import io.github.sadiqs.robot.core.Robot;
import io.github.sadiqs.robot.core.instruction.Instruction;
import io.github.sadiqs.robot.core.parser.InstructionParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
public class RobotService {

    private final InstructionParser<String> parser;

    private final InstructionProcessor<Instruction, Robot> operator;

    public List<String> executeRobotInstructions(Stream<String> rawInstructions) {
        Stream<Instruction> instructionStream = rawInstructions
                .filter(Objects::nonNull)
                .filter(not(String::isBlank))
                .map(parser::parse);

        return operator.processInstructions(instructionStream).map(this::reportingFormat).toList();
    }

    private String reportingFormat(Robot robot) {
        return robot.position().x() + "," + robot.position().y() + "," + robot.facing().name();
    }

}
