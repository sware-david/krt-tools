package io.github.swaredavid.core;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CucumberTagInterpreter {
    private static final Logger logger = LoggerFactory.getLogger(CucumberTagInterpreter.class);

    public static ArrayList<String> formatTags(ArrayList<String> tagsList) {
        ArrayList<String> formatedTags = new ArrayList<>();
        formatedTags.clear();

        tagsList.forEach(tag -> {
            for (String tSplitedAnd: tag.split("[ ]+([Aa][Nn][Dd])[ ]+")) {
                String cleanTag = tSplitedAnd.replaceAll("[ ]+([Oo][Rr])[ ]+", ",");
                cleanTag = cleanTag.replaceAll(" *([Nn][Oo][Tt]) +","~").trim();

                /* remove parentheses to  prevent error on krt core, replace with space*/
                cleanTag.replaceAll("[(]|[)]", " ");
                formatedTags.add(cleanTag);
                logger.debug("configuring tag: {}", cleanTag);
            }
        });
        return formatedTags;
    }
}
