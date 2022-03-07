package ru.javawebinar.topjava;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class StopWatchTestRule extends TestWatcher {

    private static final Logger log = LoggerFactory.getLogger(StopWatchTestRule.class);
    private final Map<String, Long> testsDuration;
    private long startTime;

    public StopWatchTestRule(Map<String, Long> testsDuration) {
        this.testsDuration = testsDuration;
    }

    @Override
    public void starting(Description description) {
        startTime = System.currentTimeMillis();
        super.starting(description);
    }

    @Override
    protected void finished(Description description) {
        String testName = description.getMethodName();
        long testDuration = System.currentTimeMillis() - startTime;
        log.info("Test {} duration: {} ms", testName, testDuration);
        testsDuration.put(testName, testDuration);
        super.finished(description);
    }
}
