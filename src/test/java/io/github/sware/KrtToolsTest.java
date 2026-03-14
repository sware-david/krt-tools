package io.github.sware;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.github.sware.tools.KrtTools;

public class KrtToolsTest {
    @Test
    public void testConfigureTags() {
        String[] tagsFormated = KrtTools.configTags("@test1 or not @test");
        Assert.assertTrue(tagsFormated.length != 0, "tags formated from class configuration");
    }

    @Test
    public void testConfigureTagsFromSystem() {
        System.setProperty("karate.options", "--tags @test-0001 or not @skipme");
        String[] tagsFormated = KrtTools.configTags("@test1 or not @test");
        Assert.assertTrue(tagsFormated.length != 0, "tags formated from class configuration");
    }

    // TODO implements complete test cases
}
