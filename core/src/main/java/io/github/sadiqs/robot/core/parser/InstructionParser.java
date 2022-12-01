package io.github.sadiqs.robot.core.parser;

import io.github.sadiqs.robot.core.instruction.Instruction;

public interface InstructionParser<T> {
    Instruction parse(T raw);
}
