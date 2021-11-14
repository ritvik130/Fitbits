package me.seg.fitbites.data;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestJUnitTests {

    @Test
    public void testFitClassTime() {
        //test time conversions
        assertEquals(660, FitClass.convertTime(11, 0));
        assertEquals(690, FitClass.convertTime(11, 30));
        assertEquals(1320, FitClass.convertTime(22, 0));
        assertEquals(800, FitClass.convertTime(13, 20));
    }
}