package io.github.sware;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.github.sware.core.FilterSystemProperties;

public class FilterSystemPropertiesTest {
    private static Logger logger = LoggerFactory.getLogger(FilterSystemPropertiesTest.class);

    @Test
    public void testGenericFilterTagsSystem() {
        List<String> results;
        StringBuilder options = new StringBuilder();
        // append options for tags with ket --tags
        options.append(" ").append("--tags @TEST-00011 or @TEST-TAG-01");
        options.append(" ").append("--tags not @TEST-0123");
        options.append(" ").append("-t @TEST-00021 or @TEST-TAG-11");
        options.append(" ").append("-t ~@TEST-TAG-123");
        // append options empty
        options.append(" ").append("-t --tags");
        // appen options with tags arg malformed
        options.append(" ").append("-t TEST-0122 -t=R@TESRIISTONE");
        options.append(" ").append("--tags TEST-0215 --tags=R@TESRIISTONE");
        // appen other karate options maybe match
        options.append(" ").append("--threads -test:ClasTestJava.java --ssl --watch --dryrun --env -e");
        System.setProperty("karate.options", options.toString());
        results = FilterSystemProperties.getTags();

        Assert.assertTrue(results.stream().allMatch(options.toString()::contains), "options filtered successfully");
        logger.info("raw options {}", options);
        logger.info("clean up options '{}'", results);
    }

    @Test
    public void testFilterTagsEmpty() {
        List<String> results;
        results = FilterSystemProperties.getTags();
        Assert.assertTrue(results.isEmpty(), "options empty undefined");

        System.setProperty("karate.options", "");
        results = FilterSystemProperties.getTags();
        Assert.assertTrue(results.isEmpty(), "options empty defined");
    }
}
