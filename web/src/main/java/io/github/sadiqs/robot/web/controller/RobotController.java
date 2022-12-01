package io.github.sadiqs.robot.web.controller;

import io.github.sadiqs.robot.web.service.RobotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class RobotController {

    final RobotService robotService;

    @PostMapping("/robot")
    @ResponseBody
    public String processRobotInstructions(@RequestBody String instructionText) {
        return String.join("\n", robotService.executeRobotInstructions(instructionText.lines()));
    }

}
