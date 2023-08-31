package com.intechcore.task3;

import com.intechcore.MemoryMeasurer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MemoryMeasurerTest {

    @Test
    public void shouldMeasureMemoryOfTwoObjectsWithReference(){
        A a = new A();
        a.b = new B();
        a.b.a = a;

        assertEquals(16, MemoryMeasurer.measure(a));
    }

    @Test
    public void shouldCalculateTwoObjectSizeWithComplexReference(){
        A a = new A();
        a.b = new B();
        a.b.a = new A();
        a.b.a.b = a.b;

        assertEquals(24, MemoryMeasurer.measure(a));
    }
}
