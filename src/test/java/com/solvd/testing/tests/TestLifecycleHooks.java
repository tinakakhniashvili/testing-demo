package com.solvd.testing.tests;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

public class TestLifecycleHooks {

    @BeforeSuite
    public void beforeSuite() {
        System.out.println("BeforeSuite: starting suite");
    }

    @AfterSuite
    public void afterSuite() {
        System.out.println("AfterSuite: ending suite");
    }

    @BeforeTest
    public void beforeTest() {
        System.out.println("BeforeTest: starting <test> block");
    }

    @AfterTest
    public void afterTest() {
        System.out.println("AfterTest: ending test <test> block");
    }
}
