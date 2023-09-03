package com.intechcore.task4;

import com.intechcore.MemoryMeasurer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MemoryMeasurerTest {

    @Test
    public void shouldMeasureThreeObjectsMemory(){
        C c = new C();
        c.a = new A();
        c.b = new B();
        assertEquals(127, MemoryMeasurer.measure(c));
    }

    @Test
    public void shouldMeasureThreeObjectsMemoryWithComplexReference(){
        C c = new C();
        c.a = new A();
        c.b = new B();
        c.a.b = new B();
        c.a.c = new C();
        c.a.c.a = c.a;
        c.a.c.b = c.b;
        assertEquals(235, MemoryMeasurer.measure(c));
    }

    @Test
    public void shouldMeasureMemoryOfThreeObjectsWithAdvancedReference(){
        C c = new C();
        c.a = new A();
        c.b = new B();
        c.a.b = new B();
        c.a.c = new C();
        c.a.c.b = c.a.b;
        c.a.b = new B();
        assertEquals(247, MemoryMeasurer.measure(c));
    }

}
