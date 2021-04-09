package com.healthycoderapp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BMICalculatorTest {

    @ParameterizedTest
    @ValueSource(doubles = {90.0, 99.9, 103, 11000})
    void shouldReturnTrue_when_DietIsRecommended(Double coderWeight) {
        //given
        double weight = coderWeight;
        double height = 1.70;

        //when
        boolean recommendation = BMICalculator.isDietRecommended(weight, height);

        //then
        assertTrue(recommendation);
    }

    //Version with Values from File
    @ParameterizedTest(name = "weight={0}, height={1}")
    @CsvFileSource(resources = "/diet-recommended-input-data.csv", numLinesToSkip = 1)
    void shouldReturnTrue_when_DietIsRecommendedFromFile(Double coderWeight, Double coderHeigth) {
        //given
        double weight = coderWeight;
        double height = coderHeigth;

        //when
        boolean recommendation = BMICalculator.isDietRecommended(weight, height);

        //then
        assertTrue(recommendation);
    }



//  @ParameterizedTest
    @ParameterizedTest(name = "weight={0}, height={1}")//this is an option for better readability in Test-Report
    @CsvSource(value = {"70,1.70", "59, 1.65", "85, 1.85"})
    void should_return_false_when_Diet_is_not_Recommended(Double coderWeight, Double coderHeight) {
        //given
        double weight = coderWeight;
        double height = coderHeight;

        //when
        boolean recommendation = BMICalculator.isDietRecommended(weight, height);

        //then
        assertFalse(recommendation);
    }


    @Test
    void should_throw_ArithmeticException_when_HeightZero() {
        //given
        double weight = 70;
        double height = 0;

        //when
        Executable executable = () -> BMICalculator.isDietRecommended(weight, height);

        //then
        assertThrows(ArithmeticException.class, executable);
    }

    @Test
    void should_returnCoderWithWorstBMI_whenCoderListNotEmpty(){
        //given
        List<Coder> coderList = new ArrayList<>();
        coderList.add(new Coder(1.80, 90, 32, Gender.MALE));
        coderList.add(new Coder(1.65, 58, 28, Gender.FEMALE));
        coderList.add(new Coder(1.72, 99, 33, Gender.MALE));

        //when
        Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coderList);

        //then
        assertAll(
                () -> assertEquals(99, coderWorstBMI.getWeight()),
                () -> assertEquals(1.72, coderWorstBMI.getHeight())
        );
    }

    @Test
    void should_returnNull_whenCoderListEmpty(){
        //given
        List<Coder> coderList = new ArrayList<>();

        //when
        Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coderList);

        //then
        assertNull(coderWorstBMI);
    }

    @Test
    void should_returnCorrectBmiArray_whenCoderListNotEmpty(){

        //given
        List<Coder> coderList = new ArrayList<>();
        coderList.add(new Coder(1.80, 90, 32, Gender.MALE));
        coderList.add(new Coder(1.65, 58, 28, Gender.FEMALE));
        coderList.add(new Coder(1.72, 99, 33, Gender.MALE));

        double[] expectedValues = {27.78, 21.30, 33.46};

        //when
        double[] coderBMIs = BMICalculator.getBMIScores(coderList);

        //then
        assertArrayEquals(expectedValues, coderBMIs);

    }
}