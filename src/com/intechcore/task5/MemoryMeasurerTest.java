package com.intechcore.task5;


import com.intechcore.MemoryMeasurer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MemoryMeasurerTest {

    @Test
    public void shouldMeasureOneComplexObject() {
        A a = new A();
        a.numbersInt = new int[]{5, 10, 15, 20, 25};
        a.name = "Robert";
        a.b = new B();
        a.c = new C();
        assertEquals(128, MemoryMeasurer.measure(a));
    }

    @Test
    public void shouldMeasureAdvancedObject() {
        A a = new A();
        a.numbersInt = new int[]{2, 55, 10, 33};
        a.name = "John";
        a.b = new B();
        a.b.numbersDouble = new double[]{2.5, 4.0, 6.3, 8.1, 10.2};
        a.b.text = "simple text";
        a.c = new C();
        a.c.letters = new char[]{'A', 'B', 'C', 'D', 'E'};
        a.c.fruits = new String[]{"Apple", "Banana"};
        assertEquals(230, MemoryMeasurer.measure(a));
    }

    @Test
    public void shouldMeasureMoreAdvancedObject() {
        A a = new A();
        a.numbersInt = new int[]{2, 3, 4, 5, 6};
        a.name = "Albert";
        a.b = new B();
        a.b.a = new A();
        a.b.a.numbersInt = new int[]{10, 11, 12, 13};
        a.b.a.c = new C();
        a.b.a.c.letters = new char[]{'g', 'z', 's'};
        a.b.a.c.fruits = new String[]{"Mango", "Grape"};
        assertEquals(218, MemoryMeasurer.measure(a));
    }
}
