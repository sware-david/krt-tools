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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author David J. Gutarra Ramos
 * @version 1.0
 * @since 1.0
 */

public class CucumberTagInterpreter {
    private static final Logger logger = LoggerFactory.getLogger(CucumberTagInterpreter.class);

    /**
     * <code>Spanish</code><br>
     * Este método realiza un formateo de las etiquetas de filtro en Cucumber para
     * transformar la sintaxis de Tag-Expression-v2 hacia Tag-Expression-v1
     * (soportado por Karate DSL). soporta los conectores lógicos {@code and},
     * {@code or} y {@code not} y sus combinaciones para retornar un arreglo en
     * formato <i>Tag-Expression-v1</i>
     * <br>
     * <br>
     * <code>English</code><br>
     * This method formats filter tags in Cucumber to transform the
     * Tag-Expression-v2 syntax to Tag-Expression-v1 (supported by Karate DSL). It
     * supports the logical connectors {@code and}, {@code or} and {@code not} and
     * their combinations to return an array in <i>Tag-Expression-v1</i> format.
     * <br>
     * 
     * @see <a href=
     *      "https://cucumber.io/docs/cucumber/api/#tag-expressions">Cucumber tag
     *      expression</a>
     * @param tagsList Receives an ArrayList containing tags with the
     *                 tag-expression-v2 syntax.<br>
     *                 Example:<br>
     *                 {@code @run and @debug or @testing and not @wip}
     * @return ArraysList with format of tag-expression-v1 (supported from Karate
     *         DSL).
     */
    public static ArrayList<String> formatTags(ArrayList<String> tagsList) {
        ArrayList<String> formatedTags = new ArrayList<>();
        formatedTags.clear();

        tagsList.forEach(tag -> {
            for (String tSplitedAnd : tag.split("[ ]+([Aa][Nn][Dd])[ ]+")) {
                String cleanTag = tSplitedAnd.replaceAll("[ ]+([Oo][Rr])[ ]+", ",");
                cleanTag = cleanTag.replaceAll(" *([Nn][Oo][Tt]) +", "~").trim();

                /* remove parentheses to prevent error on krt core, replace with space */
                cleanTag.replaceAll("[(]|[)]", " ");
                formatedTags.add(cleanTag);
                logger.debug("configuring tag: {}", cleanTag);
            }
        });
        return formatedTags;
    }
}
