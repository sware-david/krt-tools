package io.github.sware.core;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author David J. Gutarra Ramos
 * @version 1.0
 * @since 1.0
 */

public class FilterSystemProperties {
    private static final Logger logger = LoggerFactory.getLogger(FilterSystemProperties.class);
    private static final String keyOptions = "karate.options";

    public static ArrayList<String> getTags() {
        ArrayList<String> options = new ArrayList<>();
        String krtOptions = System.getProperty(keyOptions, "");
        if (!krtOptions.isEmpty()) {
            logger.trace("get krt options from system: {} = '{}'", keyOptions, krtOptions);
            List.of(krtOptions.split("^(--|-)| +(--|-)")).forEach(opt -> {
                if (opt.startsWith("tags") || opt.startsWith("t")) {
                    String clearOption = opt.replaceAll("^(tags|t[ ]*?(?=@))", "").trim();
                    if (!clearOption.isEmpty() && clearOption.startsWith("@")) {
                        options.add(clearOption);
                        logger.debug("tags added: {}", clearOption);
                    } else {
                        logger.warn("the option can be omitted {}", clearOption);
                    }
                }
            });

            removeTagsFromOptions();
        }
        return options;
    }

    private static void removeTagsFromOptions() {
        String krtOptions = System.getProperty(keyOptions);
        krtOptions = krtOptions.replaceAll("(--tags|-t[ ]*@).*?(?=( -|$))", "").trim();
        System.setProperty(keyOptions, krtOptions);
        logger.debug("clear options has been completed.");
        logger.trace("new value of {} is: {}", keyOptions, krtOptions);
    }
}
