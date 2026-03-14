package io.github.sware;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.github.sware.core.CucumberTagInterpreter;

public class CucumberTagInterpreterTest {
    @Test
    public void testFormatTags() {
        ArrayList<String> plainTextTags = new ArrayList<>();
        plainTextTags.add("@TEST-1 and @TEST-2 or @TEST-3 or not @TEST-4");
        List<String> results = CucumberTagInterpreter.formatTags(plainTextTags);

        Assert.assertEquals(results.size(), getAndOperatorCount(plainTextTags));

        Assert.assertEquals(getOrOperatorResultCount(results), getOrOperatorCount(plainTextTags));

        Assert.assertEquals(getNotOperatorResultCount(results), getNotOperatorCount(plainTextTags));
    }

    @Test
    public void testAndOperator() {
        ArrayList<String> plainTextTags = new ArrayList<>();
        String andOperator;
        List<String> results;

        andOperator = "and";
        plainTextTags.add(String.format("@TEST-1 %s @TEST-2", andOperator));
        results = CucumberTagInterpreter.formatTags(plainTextTags);

        Assert.assertEquals(results.size(), getAndOperatorCount(plainTextTags),
                String.format("Test '%s' operator", andOperator));
        plainTextTags.clear();

        andOperator = "And";
        plainTextTags.add(String.format("@TEST-1 %s @TEST-2", andOperator));
        results = CucumberTagInterpreter.formatTags(plainTextTags);

        Assert.assertEquals(results.size(), getAndOperatorCount(plainTextTags),
                String.format("Test '%s' operator", andOperator));
        plainTextTags.clear();

        andOperator = "aNd";
        plainTextTags.add(String.format("@TEST-1 %s @TEST-2", andOperator));
        results = CucumberTagInterpreter.formatTags(plainTextTags);

        Assert.assertEquals(results.size(), getAndOperatorCount(plainTextTags),
                String.format("Test '%s' operator", andOperator));
        plainTextTags.clear();

        andOperator = "ANd";
        plainTextTags.add(String.format("@TEST-1 %s @TEST-2", andOperator));
        results = CucumberTagInterpreter.formatTags(plainTextTags);

        Assert.assertEquals(results.size(), getAndOperatorCount(plainTextTags),
                String.format("Test '%s' operator", andOperator));
        plainTextTags.clear();

        andOperator = "anD";
        plainTextTags.add(String.format("@TEST-1 %s @TEST-2", andOperator));
        results = CucumberTagInterpreter.formatTags(plainTextTags);

        Assert.assertEquals(results.size(), getAndOperatorCount(plainTextTags),
                String.format("Test '%s' operator", andOperator));
        plainTextTags.clear();

        andOperator = "AnD";
        plainTextTags.add(String.format("@TEST-1 %s @TEST-2", andOperator));
        results = CucumberTagInterpreter.formatTags(plainTextTags);

        Assert.assertEquals(results.size(), getAndOperatorCount(plainTextTags),
                String.format("Test '%s' operator", andOperator));
        plainTextTags.clear();

        andOperator = "aND";
        plainTextTags.add(String.format("@TEST-1 %s @TEST-2", andOperator));
        results = CucumberTagInterpreter.formatTags(plainTextTags);

        Assert.assertEquals(results.size(), getAndOperatorCount(plainTextTags),
                String.format("Test '%s' operator", andOperator));
        plainTextTags.clear();

        andOperator = "AND";
        plainTextTags.add(String.format("@TEST-1 %s @TEST-2", andOperator));
        results = CucumberTagInterpreter.formatTags(plainTextTags);

        Assert.assertEquals(results.size(), getAndOperatorCount(plainTextTags),
                String.format("Test '%s' operator", andOperator));
        plainTextTags.clear();

        plainTextTags.add("@TST-001 and @TST-COV-01 and not @skipme and not @env");
        results = CucumberTagInterpreter.formatTags(plainTextTags);

        Assert.assertEquals(results.size(), getAndOperatorCount(plainTextTags), "Test multiple and operator");
        plainTextTags.clear();
    }

    @Test
    public void testOrOperator() {
        ArrayList<String> plainTextTags = new ArrayList<>();
        plainTextTags.add("@TEST-1 OR @TEST-2 or @TEST-3 oR @TEST-4 Or @TESTME");
        List<String> results = CucumberTagInterpreter.formatTags(plainTextTags);

        Assert.assertEquals(getOrOperatorResultCount(results), getOrOperatorCount(plainTextTags));
    }

    @Test
    public void testNotOperator() {
        ArrayList<String> plainTextTags = new ArrayList<>();
        plainTextTags.add(
                "@TEST-1 not @TEST-2 Not @TEST-3 nOt @TEST-4 NOt @TEST-5 nOT @TEST-6 NoT @TEST-7 nOT @TEST-8 NOT @TESTME");
        List<String> results = CucumberTagInterpreter.formatTags(plainTextTags);

        Assert.assertEquals(getNotOperatorResultCount(results), getNotOperatorCount(plainTextTags));
    }

    private int getAndOperatorCount(List<String> plainTextTags) {
        // count and operator
        // init value to represents first element on list
        return getOperatorCount(plainTextTags, " +([Aa][Nn][Dd]) +", 1);
    }

    private int getOrOperatorCount(List<String> plainTextTags) {
        // count or operator
        return getOperatorCount(plainTextTags, " +([Oo][Rr]) +");
    }

    private int getOrOperatorResultCount(List<String> results) {
        // count or operator in results
        return getOperatorCount(results, ",");
    }

    private int getNotOperatorCount(List<String> plainTextTags) {
        // count not operator
        return getOperatorCount(plainTextTags, " *([Nn][Oo][Tt]) +");
    }

    private int getNotOperatorResultCount(List<String> results) {
        // count not operator in results
        return getOperatorCount(results, "~");
    }

    private int getOperatorCount(List<String> plainTextTags, String regex) {
        AtomicInteger operatorCount = new AtomicInteger(0);
        plainTextTags.forEach(tgs -> {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(tgs);
            while (matcher.find()) {
                operatorCount.addAndGet(1);
            }
        });
        return operatorCount.get();
    }

    private int getOperatorCount(List<String> plainTextTags, String regex, int initValue) {
        AtomicInteger operatorCount = new AtomicInteger(initValue);
        plainTextTags.forEach(tgs -> {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(tgs);
            while (matcher.find()) {
                operatorCount.addAndGet(1);
            }
        });
        return operatorCount.get();
    }
}
