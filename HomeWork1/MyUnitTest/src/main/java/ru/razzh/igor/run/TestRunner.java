package ru.razzh.igor.run;

import ru.razzh.igor.annotation.*;
import ru.razzh.igor.test.Tests;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {
    public static void runTests(Class c) {
        throwExceptionIfAnnotationNotUnique(c, BeforeSuite.class);
        throwExceptionIfAnnotationNotUnique(c, AfterSuite.class);
        throwExceptionIfAnnotationNotUnique(c, BeforeTest.class);
        throwExceptionIfAnnotationNotUnique(c, AfterTest.class);

        try {
            runMethods(c);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static void runMethods(Class c) throws InvocationTargetException, IllegalAccessException {
        Tests tests = new Tests();
        Method[] methods = c.getMethods();
        Method beforeSuiteMethod = findMethodWithAnnotation(methods, BeforeSuite.class);
        Method afterSuiteMethod = findMethodWithAnnotation(methods, AfterSuite.class);
        Method beforeTestMethod = findMethodWithAnnotation(methods, BeforeTest.class);
        Method afterTestMethod = findMethodWithAnnotation(methods, AfterTest.class);

        callAnyMethod(beforeSuiteMethod, tests);

        runMethodsMarkTestOnPriority(getMethodsWihTest(methods, Test.class), beforeTestMethod, afterTestMethod, tests);

        callAnyMethod(afterSuiteMethod, tests);
    }

    private static void runMethodsMarkTestOnPriority(List<Method> methods, Method beforeTestMethod, Method afterTestMethod,
                                                     Tests tests) throws InvocationTargetException, IllegalAccessException {
        methods.sort((m1, m2) -> m2.getDeclaredAnnotation(Test.class).priority() - m1.getDeclaredAnnotation(Test.class).priority());
        for (Method m : methods) {
            if (m != null) {
                callAnyMethod(beforeTestMethod, tests);
                callAnyMethod(m, tests);
                callAnyMethod(afterTestMethod, tests);
            }
        }
    }

    private static ArrayList<Method> getMethodsWihTest(Method[] methods, Class annotation) {
        ArrayList<Method> meths = new ArrayList<>();
        for (Method m : methods ) {
            if (m.isAnnotationPresent(annotation)) {
                meths.add(m);
            }
        }
        return meths;
    }

    private static Method findMethodWithAnnotation(Method[] methods, Class annotation) {
        for (Method m : methods ) {
            if (m.isAnnotationPresent(annotation)) {
                return m;
            }
        }
        return null;
    }

    private static void throwExceptionIfAnnotationNotUnique(Class c, Class<?> annotation) {
        if(!checksUniqueMethodMarkAnnotation(c, annotation)) {
            throw new RuntimeException("Only one method can be marked with @" + annotation.getSimpleName() +
                    " annotation in a class");
        }
    }

    private static boolean checksUniqueMethodMarkAnnotation(Class clazz, Class annotation) {
        Method[] methods = clazz.getDeclaredMethods();
        int count = 0;
        for (Method method : methods) {
            if (method.isAnnotationPresent(annotation)) {
                count++;
            }
        }
        if (count > 1) {
            return false;
        }
        return true;
    }

    private static void callMethodWithParametrs(Method meth, String param, Tests tests) throws InvocationTargetException, IllegalAccessException {
        Parameter[] params = meth.getParameters();
        String[] stringParams = param.split(",");
        Object[] args = new Object[params.length];
        if (stringParams.length != params.length) {
            throw new RuntimeException("Wrong number of parameters");
        }
        Class paramType = null;
        for(int i = 0; i < params.length; i++) {
            paramType = params[i].getType();
            String strParam = stringParams[i].trim();
            if (paramType == int.class) {
                args[i] = Integer.valueOf(strParam);
            } else if (paramType == long.class) {
                args[i] = Long.valueOf(strParam);
            } else if (paramType == float.class) {
                args[i] = Float.valueOf(strParam);
            } else if (paramType == double.class) {
                args[i] = Double.valueOf(strParam);
            } else if (paramType == boolean.class) {
                args[i] = Boolean.valueOf(strParam);
            } else if (paramType == String.class) {
                args[i] = strParam;
            } else {
                args[i] = params[i].getType().cast(strParam);
            }
        }
        meth.invoke(tests, args);
    }

    private static void callAnyMethod(Method meth, Tests tests) throws InvocationTargetException, IllegalAccessException {

        if (meth.isAnnotationPresent(CsvSource.class) && meth != null) {
            String param = meth.getAnnotation(CsvSource.class).parametrs();
            callMethodWithParametrs(meth, param, tests);
        } else {
            if(meth != null) {
                meth.invoke(tests);
            }
        }
    }

}
