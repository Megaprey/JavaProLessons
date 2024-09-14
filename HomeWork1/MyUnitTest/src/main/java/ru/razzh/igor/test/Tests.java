package ru.razzh.igor.test;

import ru.razzh.igor.annotation.*;

public class Tests {

    @BeforeSuite
    public void doBeforeAllTests() {
        System.out.println("-------------------");
        System.out.println("do before all tests");
    }

    @AfterSuite
    public void doAfterAllTests() {
        System.out.println("do after all tests");
        System.out.println("-------------------");
    }

    @BeforeTest
    public void doBeforeEachTests() {
        System.out.println("@@@@@@@@@@@@@@@@@@@@");
        System.out.println("do before each tests");
    }

    @AfterTest
    public void doAfterEachTests() {
        System.out.println("do after each tests");
        System.out.println("$$$$$$$$$$$$$$$$$$$");
    }

    @Test(priority = 1)
    public void priorityIsOneTest() {
        System.out.println("-------------------");
        System.out.println("приоритет равен 1");
        System.out.println("-------------------");
    }

    @Test
    public void priorityIsFiveTest() {
        System.out.println("-------------------");
        System.out.println("приоритет равен 5");
        System.out.println("-------------------");
    }

    @Test(priority = 4)
    public void priorityIsFourTest() {
        System.out.println("-------------------");
        System.out.println("приоритет равен 4");
        System.out.println("-------------------");
    }

    @Test(priority = 3)
    public void priorityIsThreeTest() {
        System.out.println("-------------------");
        System.out.println("приоритет равен 3");
        System.out.println("-------------------");
    }

    @Test(priority = 7)
    @CsvSource(parametrs = "10, Java, 20, true")
    public void priorityIsSevenTest(int a, String b, int c, boolean d) {
        System.out.println("-------------------");
        System.out.println("приоритет равен 7. Вызван с параметрами: " + a + ", " + b + ", " + c + ", " + d);
        System.out.println("-------------------");
    }
}