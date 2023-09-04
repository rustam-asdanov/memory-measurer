package com.intechcore.task2;

import com.intechcore.MemoryMeasurer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MemoryMeasurerTest {

    @Test
    public void shouldMeasureMemoryOfTwoObjectsWithReference(){
        A a1 = new A();
        A a2 = new A();
        a2.b = new B();

        assertEquals(12, MemoryMeasurer.measure(a1));
        assertEquals(20, MemoryMeasurer.measure(a2));
    }
}
