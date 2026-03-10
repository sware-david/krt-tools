package io.github.swaredavid.tools;

import java.util.ArrayList;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.swaredavid.core.CucumberTagInterpreter;
import io.github.swaredavid.core.FilterSystemProperties;


public class KrtTools {
    private static final Logger logger = LoggerFactory.getLogger(KrtTools.class);

    private KrtTools() {
    }

    /**
     * Esta función permite tener soporte para la sintaxis 
     * @param tgs
     * @return
     */
    public static String[] from(String... tgs) {
        ArrayList<String> tags;
        /* priority: 1. System properties 2. tags from arguments */
        tags = FilterSystemProperties.getTags();
        if (tags.isEmpty()) {
            tags = new ArrayList<>(Arrays.asList(tgs));
        } else {
            logger.debug("tags configured from system properties");
        }

        /* clear tags */
        tags.removeIf(String::isBlank);
        tags.removeIf(String::isEmpty);

        ArrayList<String> formattedTags = CucumberTagInterpreter.formatTags(tags);
        logger.info("tags formatted: {}", formattedTags);
        return formattedTags.toArray(new String[0]);
    }
}
