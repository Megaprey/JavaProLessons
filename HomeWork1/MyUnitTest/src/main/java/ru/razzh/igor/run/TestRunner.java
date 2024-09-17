package ru.razzh.igor.run;

import ru.razzh.igor.annotation.*;
import ru.razzh.igor.pojo.MethodContainer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {
    public static void runTests(Class c) throws Exception {
        runMethods(c);
    }

    private static void runMethods(Class c) throws Exception {
        Object instance = c.getConstructor().newInstance();
        Method[] methods = c.getMethods();

        MethodContainer methodContainer = getMethodsWithAnnotation(methods);
        Method beforeSuiteMethod = methodContainer.getBeforSuiteMethod();
        Method afterSuiteMethod = methodContainer.getAfterSuiteMethod();

        callAnyMethod(beforeSuiteMethod, instance);

        runMethodsMarkTestOnPriority(methodContainer, instance);

        callAnyMethod(afterSuiteMethod, instance);
    }

    private static void runMethodsMarkTestOnPriority(MethodContainer methodContainer,
                                                     Object instance) throws InvocationTargetException, IllegalAccessException {
        ArrayList<Method> marksTestMethods = methodContainer.getTestMethods();
        marksTestMethods.sort((m1, m2) -> m2.getDeclaredAnnotation(Test.class).priority() - m1.getDeclaredAnnotation(Test.class).priority());
        for (Method m : marksTestMethods) {
            if (m != null) {
                for (Method beforeTestMethod : methodContainer.getBeforeTestMethods()) {
                    callAnyMethod(beforeTestMethod, instance);
                }
                callAnyMethod(m, instance);
                for (Method afterTestMethod : methodContainer.getAfterTestMethods()) {
                    callAnyMethod(afterTestMethod, instance);
                }
            }
        }
    }


    private static MethodContainer getMethodsWithAnnotation(Method[] methods) {
        MethodContainer methodContainer = new MethodContainer();
        methodContainer.setTestMethods(new ArrayList<>());
        methodContainer.setBeforeTestMethods(new ArrayList<>());
        methodContainer.setAfterTestMethods(new ArrayList<>());
        int afterSuiteCount = 0;
        int beforeSuiteCount = 0;
        for (Method m : methods ) {
            if (m.isAnnotationPresent(AfterSuite.class)) {
                afterSuiteCount++;
                if (afterSuiteCount > 1) {
                    throw new RuntimeException("Only one method can be marked with @AfterSuite annotation in a class");
                }
                methodContainer.setAfterSuiteMethod(m);
            } else if (m.isAnnotationPresent(BeforeSuite.class)) {
                beforeSuiteCount++;
                if (beforeSuiteCount > 1) {
                    throw new RuntimeException("Only one method can be marked with @BeforeSuite annotation in a class");
                }
                methodContainer.setBeforeSuiteMethod(m);
            } else if (m.isAnnotationPresent(BeforeTest.class)) {
                methodContainer.getBeforeTestMethods().add(m);
            } else if (m.isAnnotationPresent(AfterTest.class)) {
                methodContainer.getAfterTestMethods().add(m);
            } else if (m.isAnnotationPresent(Test.class)) {
                methodContainer.getTestMethods().add(m);
            }
        }
        return methodContainer;
    }




    private static void callMethodWithParametrs(Method meth, String param, Object instance) throws InvocationTargetException, IllegalAccessException {
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
        meth.invoke(instance, args);
    }

    private static void callAnyMethod(Method meth, Object instance) throws InvocationTargetException, IllegalAccessException {

        if (meth != null && meth.isAnnotationPresent(CsvSource.class)) {
            String param = meth.getAnnotation(CsvSource.class).parametrs();
            callMethodWithParametrs(meth, param, instance);
        } else if(meth != null) {
            meth.invoke(instance);
        }
    }

}
