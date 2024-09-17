package ru.razzh.igor.pojo;

import ru.razzh.igor.annotation.AfterSuite;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class MethodContainer {
    Method beforSuiteMethod;
    Method afterSuiteMethod;
    ArrayList<Method> beforeTestMethods;
    ArrayList<Method> afterTestMethods;

    public void setBeforeSuiteMethod(Method beforSuiteMethod) {
        this.beforSuiteMethod = beforSuiteMethod;
    }

    public void setAfterSuiteMethod(Method afterSuiteMethod) {
        this.afterSuiteMethod = afterSuiteMethod;
    }

    public void setBeforeTestMethods(ArrayList<Method> beforeTestMethods) {
        this.beforeTestMethods = beforeTestMethods;
    }

    public void setAfterTestMethods(ArrayList<Method> afterTestMethods) {
        this.afterTestMethods = afterTestMethods;
    }

    public void setTestMethods(ArrayList<Method> testMethods) {
        this.testMethods = testMethods;
    }

    public ArrayList<Method> getTestMethods() {
        return testMethods;
    }

    public ArrayList<Method> getAfterTestMethods() {
        return afterTestMethods;
    }

    public ArrayList<Method> getBeforeTestMethods() {
        return beforeTestMethods;
    }

    public Method getAfterSuiteMethod() {
        return afterSuiteMethod;
    }

    public Method getBeforSuiteMethod() {
        return beforSuiteMethod;
    }

    ArrayList<Method> testMethods;

}
