/**
 * Copyright 2026 David Gutarra R.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.sware.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author David J. Gutarra Ramos
 * @version 1.0
 * @since 1.0
 */

public class FilterSystemProperties {
    private FilterSystemProperties() {
    }

    private static final Logger logger = LoggerFactory.getLogger(FilterSystemProperties.class);
    private static final String KEY_OPTIONS = "karate.options";

    /**
     * <code>Spanish</code><br>
     * Este método realiza el filtrado de las opciones de Karate desde las
     * propiedades del sistema <code>-Dkarate.options</code>, filtra las opciones de
     * las etiquetas <code>-t</code> y <code>--tags</code> recupera los valores y
     * los almacena para posteriormente retornarlos, haciendo una validación mínima
     * de los valores de las opciones. Finalmente realiza una limpieza de las
     * opciones de Karate DSL.
     * <br>
     * <br>
     * <code>English</code><br>
     * This method filters the Karate options from the system properties
     * <code>-Dkarate.options</code>, filters the options from the <code>-t</code>
     * and <code>--tags</code> labels, retrieves the values, and stores them to
     * return them later, performing a minimal validation of the option values.
     * Finally, it cleans up the Karate options of Karate DSL.
     * <br>
     * 
     * @see <a href=
     *      "https://github.com/karatelabs/karate/tree/v1.5.2?tab=readme-ov-file#karateoptions">Karate
     *      DLS options</a>
     * 
     * @return List of tags configured from system properties, for example
     *         <code>-Dkarate.options=... --tags @example1 or ... @etc</code>
     */
    public static List<String> getTags() {
        List<String> options = new ArrayList<>();
        String krtOptions = System.getProperty(KEY_OPTIONS, "");
        if (!krtOptions.isEmpty()) {
            logger.trace("get krt options from system: {} = '{}'", KEY_OPTIONS, krtOptions);
            AtomicReference<String> newKrtOptions = new AtomicReference<>(krtOptions);
            List.of(krtOptions.split(" *(--|-)")).forEach(opt -> {
                if (opt.startsWith("tags") || opt.startsWith("t")) {
                    String clearOption = opt.replaceAll("^((tags|t)=? *(?=[~@]|not))", "").trim();
                    if (clearOption.startsWith("@") || clearOption.startsWith("~") || clearOption.startsWith("not")) {
                        options.add(clearOption);
                        newKrtOptions.set(newKrtOptions.get().replaceFirst(" *(--tags|-t) *" + clearOption, ""));
                        logger.debug("tags added: {}", clearOption);
                    } else if (clearOption.replaceFirst("(tags|t)", "").isEmpty()) {
                        newKrtOptions.set(newKrtOptions.get().replaceFirst(" *(--tags|-t)", ""));
                        logger.warn("removing tag option empty '{}'", opt);
                    } else if (opt.matches("^((tags|t)[= ]).+")) {
                        newKrtOptions.set(newKrtOptions.get().replaceFirst(" *(--|-)" + opt, ""));
                        logger.warn("removing tag option malformed '{}'", opt);
                    } else {
                        logger.debug("the option can be omitted '{}'", opt);
                    }
                }
            });

            updateOptions(newKrtOptions.get());
        }
        return options;
    }

    /**
     * This method cleans up the <code>karate.options</code> system property at
     * runtime.
     */
    private static void updateOptions(String krtNewOptions) {
        System.setProperty(KEY_OPTIONS, krtNewOptions);
        logger.debug("clear options has been completed.");
        logger.trace("new value of {} is: {}", KEY_OPTIONS, krtNewOptions);
    }
}
