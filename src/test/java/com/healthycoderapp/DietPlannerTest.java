package com.healthycoderapp;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class DietPlannerTest {

    private DietPlanner dietPlanner;

    @BeforeAll
    static void beforeAll(){//bevorAll must be static
        System.out.println("starting tests");
    }

    @AfterAll
    static void afterAll(){//bevorAll must be static
        System.out.println("done testing, closing all connections");
    }

    @BeforeEach
    void setUp() {
        this.dietPlanner = new DietPlanner(20, 30, 50);
    }

    @AfterEach
    void afterEach(){
        System.out.println("a unittest has finished");
    }

    @RepeatedTest(3)
    void shouldReturnCorrectDietPlan_when_givenCorrectCoder(){
        //given
        Coder coder = new Coder(1.82, 75.0, 26, Gender.MALE);
        DietPlan expected = new DietPlan(2202, 110, 73, 275);

        //when
        DietPlan actual = dietPlanner.calculateDiet(coder);

        //then
        assertAll(
                () -> assertEquals(actual.getCalories(), expected.getCalories()),
                () -> assertEquals(actual.getCarbohydrate(), expected.getCarbohydrate()),
                () -> assertEquals(actual.getFat(), expected.getFat()),
                () -> assertEquals(actual.getProtein(), expected.getProtein())
        );
    }
}