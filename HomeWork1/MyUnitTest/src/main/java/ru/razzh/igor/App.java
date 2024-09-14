package ru.razzh.igor;

import ru.razzh.igor.run.TestRunner;
import ru.razzh.igor.test.Tests;

public class App
{
    public static void main( String[] args )
    {
        TestRunner.runTests(Tests.class);
    }
}
