package io.github.sadiqs.robot.web.controller;

import io.github.sadiqs.robot.web.service.RobotService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RobotController.class)
class RobotControllerTest {

    @MockBean
    private RobotService robotService;

    @Autowired
    private MockMvc mockMvc;

    @Captor
    private ArgumentCaptor<Stream<String>> instructionsCaptor;

    @AfterEach
    void tearDown() {
        Mockito.reset(robotService);
    }

    @Test
    void testControllerShouldSplitThePayloadInstructions() throws Exception {
        mockMvc.perform(post("/robot").content("""
                        the
                        bulk
                        instructions
                        """).contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Mockito.verify(robotService).executeRobotInstructions(instructionsCaptor.capture());

        var individualInstructions = instructionsCaptor.getValue().toList();
        assertThatList(individualInstructions)
                .hasSize(3)
                .containsExactly("the", "bulk", "instructions");
    }

    @Test
    void testControllerShouldAssembleAllTheReports() throws Exception {
        Mockito.when(robotService.executeRobotInstructions(ArgumentMatchers.any())).thenReturn(List.of("foo", "blah"));

        String response = mockMvc.perform(post("/robot").content("blah").contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(response).isEqualTo("""
                foo
                blah""");
    }
}