package io.github.sadiqs.robot.web.config;

import io.github.sadiqs.robot.core.InstructionProcessor;
import io.github.sadiqs.robot.core.Robot;
import io.github.sadiqs.robot.core.RobotOperator;
import io.github.sadiqs.robot.core.instruction.Instruction;
import io.github.sadiqs.robot.core.model.Table;
import io.github.sadiqs.robot.core.parser.InstructionParser;
import io.github.sadiqs.robot.core.parser.TextInstructionParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public Table defaultTable(@Value("${table.width}") int width, @Value("${table.height}") int height) {
        return new Table(width, height);
    }

    @Bean
    public InstructionParser<String> parser() {
        return new TextInstructionParser();
    }

    @Bean
    public InstructionProcessor<Instruction, Robot> operator(Table table) {
        return new RobotOperator(table);
    }
}
