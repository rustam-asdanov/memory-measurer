package com.intechcore.task1;

import com.intechcore.MemoryMeasurer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MemoryMeasurerTest {

    @Test
    public void shouldMeasureMemoryOfTwoObjects() {
        A a = new A();
        B b = new B();
        assertEquals(12, MemoryMeasurer.measure(a));
        assertEquals(24, MemoryMeasurer.measure(b));
    }
}
