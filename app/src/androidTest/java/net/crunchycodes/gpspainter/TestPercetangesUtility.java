package net.crunchycodes.gpspainter;


import net.crunchycodes.gpspainter.utility.PercentagesUtility;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TestPercetangesUtility {

    @Test
    public void testValues0() {
        PercentagesUtility percentagesUtility = new PercentagesUtility(1, 20, 1);

        int[] values = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
        ArrayList<Integer> expectedValues = new ArrayList<>();
        for (int value : values) expectedValues.add(value);

        ArrayList<Integer> actualValues = new ArrayList<>();
        for (int i = 0; i < 101; ++i) {
            int value = percentagesUtility.value(i);
            if (actualValues.contains(value)) continue;
            actualValues.add(value);
        }

        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testValues1() {
        PercentagesUtility percentagesUtility = new PercentagesUtility(1, 11, 2);

        int[] values = new int[]{1, 3, 5, 7, 9, 11};
        ArrayList<Integer> expectedValues = new ArrayList<>();
        for (int value : values) expectedValues.add(value);

        ArrayList<Integer> actual = new ArrayList<>();
        for (int i = 0; i < 101; ++i) {
            int value = percentagesUtility.value(i);
            if (actual.contains(value)) continue;
            actual.add(value);
        }

        assertEquals(expectedValues, actual);
    }


    @Test
    public void testValues2() {
        PercentagesUtility percentagesUtility = new PercentagesUtility(10, 22, 3);

        int[] values = new int[]{10, 13, 16, 19, 22};
        ArrayList<Integer> expectedValues = new ArrayList<>();
        for (int value : values) expectedValues.add(value);

        ArrayList<Integer> actualValues = new ArrayList<>();
        for (int i = 0; i < 101; ++i) {
            int value = percentagesUtility.value(i);
            if (actualValues.contains(value)) continue;
            actualValues.add(value);
        }

        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testValues3() {
        PercentagesUtility percentagesUtility = new PercentagesUtility(4, 24, 4);

        int[] values = new int[]{4, 8, 12, 16, 20, 24};
        ArrayList<Integer> expectedValues = new ArrayList<>();
        for (int value : values) expectedValues.add(value);

        ArrayList<Integer> actualValues = new ArrayList<>();
        for (int i = 0; i < 101; ++i) {
            int value = percentagesUtility.value(i);
            if (actualValues.contains(value)) continue;
            actualValues.add(value);
        }

        assertEquals(expectedValues, actualValues);
    }
}
