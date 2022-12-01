package io.github.sadiqs.robot.core;

import java.util.stream.Stream;

public interface InstructionProcessor<I, O> {
    Stream<O> processInstructions(Stream<I> instructionStream);

}