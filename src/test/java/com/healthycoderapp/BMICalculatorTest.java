package com.healthycoderapp;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.DisabledOnJre;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

class BMICalculatorTest {

    private String environment = "dev";

    @Nested //With nested annotation, innerclasses can be used to group tests, related to a method, for better overview
    class isDietRecommendedTests {
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
        @DisabledOnOs(OS.MAC)//to skip a test for a certain os, there is also a version for skipping at certain jre's
        void should_throw_ArithmeticException_when_HeightZero() {
            //given
            double weight = 70;
            double height = 0;

            //when
            Executable executable = () -> BMICalculator.isDietRecommended(weight, height);

            //then
            assertThrows(ArithmeticException.class, executable);
        }

    }

    @Nested
    class findCoderWithWorstBMITests{
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
        void shouldReturnCoderWithWorstBmi_inUnder5ms_WhenCodersListContains1000Elements(){
            //given
            Assumptions.assumeTrue(BMICalculatorTest.this.environment == "prod");//this can be used when a certain test doesn't make sense on ex develop machine

            List<Coder> codersList = new ArrayList<>();
            double minWeight = 20.0;
            double maxWeight = 180.0;

            double minHeight = 20.0;
            double maxHeight = 180.0;

            for(int i = 0; i < 1000; i++){
                codersList.add(new Coder(
                        ThreadLocalRandom.current().nextDouble(minHeight, maxHeight),
                        ThreadLocalRandom.current().nextDouble(minWeight, maxWeight)
                ));
            }
            //when
            Executable executable = () -> BMICalculator.findCoderWithWorstBMI(codersList);

            //then
            assertTimeout(Duration.ofMillis(5), executable);
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
    }

    @Nested
    class getBMIScoresTests{
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


}