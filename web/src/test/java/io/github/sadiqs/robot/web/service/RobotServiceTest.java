package io.github.sadiqs.robot.web.service;

import io.github.sadiqs.robot.core.InstructionProcessor;
import io.github.sadiqs.robot.core.Robot;
import io.github.sadiqs.robot.core.instruction.Instruction;
import io.github.sadiqs.robot.core.instruction.Place;
import io.github.sadiqs.robot.core.instruction.Report;
import io.github.sadiqs.robot.core.instruction.RobotInstruction;
import io.github.sadiqs.robot.core.model.Direction;
import io.github.sadiqs.robot.core.model.Position;
import io.github.sadiqs.robot.core.parser.InstructionParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
class RobotServiceTest {

    @Mock
    InstructionParser<String> parser;

    @Mock
    InstructionProcessor<Instruction, Robot> operator;

    @Captor
    ArgumentCaptor<String> rawInstructionCaptor;

    @Captor
    ArgumentCaptor<Stream<Instruction>> instructionCaptor;

    @InjectMocks
    RobotService robotService;

    @AfterEach
    @SuppressWarnings("unchecked")
    void tearDown() {
        Mockito.reset(parser, operator);
    }

    private void setupOperatorBehavior(Function<Stream<Instruction>, Stream<Robot>> behavior) {
        Mockito.when(operator.processInstructions(any())).then(call -> {
            Stream<Instruction> rawInstructions = call.getArgument(0);
            return behavior.apply(rawInstructions);
        });
    }

    @Test
    void testShouldPassTheRawInstructionsToTheParser() {

        setupOperatorBehavior(rawInstructions -> rawInstructions.flatMap(x -> Stream.empty())); // just drain

        var rawInstructions = List.of("A", "B", "C");

        robotService.executeRobotInstructions(rawInstructions.stream());
        Mockito.verify(parser, times(3)).parse(rawInstructionCaptor.capture());

        var actualArguments = rawInstructionCaptor.getAllValues();
        assertThatList(actualArguments).containsExactlyElementsOf(rawInstructions);
    }

    @Test
    void testShouldPassTheParsedInstructionsToOperator() {
        var rawInstructions = List.of("A", "B", "C");

        var instructions = List.of(new Place(new Position(0, 0), Direction.NORTH),
                RobotInstruction.MOVE,
                new Report());

        Mockito.when(parser.parse(any())).thenReturn(instructions.get(0), instructions.stream().skip(1).toArray(Instruction[]::new));

        robotService.executeRobotInstructions(rawInstructions.stream());

        Mockito.verify(operator).processInstructions(instructionCaptor.capture());
        assertThatList(instructionCaptor.getValue().toList()).containsExactlyElementsOf(instructions);
    }

    @Test
    void testShouldFormatAsPerTheReportingFormat() {
        Mockito.when(parser.parse(any())).thenReturn(RobotInstruction.LEFT);
        Mockito.when(operator.processInstructions(any())).thenReturn(Stream.of(new Robot(new Position(1, 2), Direction.SOUTH)));

        List<String> output = robotService.executeRobotInstructions(Stream.of("foo"));

        assertThatList(output).containsExactly("1,2,SOUTH");
    }
}