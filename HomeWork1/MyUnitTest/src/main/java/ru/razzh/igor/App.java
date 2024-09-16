package ru.razzh.igor;

import ru.razzh.igor.run.TestRunner;
import ru.razzh.igor.test.Tests;

import java.lang.reflect.InvocationTargetException;

public class App
{
    public static void main( String[] args ) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        TestRunner.runTests(Tests.class);
    }
}
