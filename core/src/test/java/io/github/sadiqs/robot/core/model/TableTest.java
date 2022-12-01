package io.github.sadiqs.robot.core.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;


class TableTest {

    Table table = new Table(2, 5);

    @ParameterizedTest
    @CsvSource({
            "0,0,true",
            "0,1,true",
            "2,2,false",
            "0,3,true",
            "3,3,false",
            "-3,3,false",
            "3,-4,false",
            "0,-1,false",
    })
    void testTableShouldKnowIfPositionIsOnTheTable(int x, int y, boolean expected) {
        var position = new Position(x, y);
        var onTheTable = table.isOnTheTable(position);

        assertThat(onTheTable).isEqualTo(expected);
    }
}