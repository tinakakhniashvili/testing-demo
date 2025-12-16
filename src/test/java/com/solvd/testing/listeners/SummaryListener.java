package com.solvd.testing.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.concurrent.atomic.AtomicInteger;

public class SummaryListener implements ITestListener {

    private final AtomicInteger passed = new AtomicInteger();
    private final AtomicInteger failed = new AtomicInteger();
    private final AtomicInteger skipped = new AtomicInteger();

    @Override
    public void onTestSuccess(ITestResult result) {
        passed.incrementAndGet();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        failed.incrementAndGet();
        Throwable t = result.getThrowable();
        System.out.println("FAILED: " + result.getName() + " :: " + (t == null ? "" : t.toString()));
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        skipped.incrementAndGet();
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("SUMMARY: passed=" + passed.get() + ", failed=" + failed.get() + ", skipped=" + skipped.get());
    }
}
