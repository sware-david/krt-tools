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

package io.github.sware.tools;

import java.util.ArrayList;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.sware.core.CucumberTagInterpreter;
import io.github.sware.core.FilterSystemProperties;

/**
 * @author David J. Gutarra Ramos
 * @version 1.0
 * @since 1.0
 */

public class KrtTools {
    private static final Logger logger = LoggerFactory.getLogger(KrtTools.class);

    private KrtTools() {
    }

    /**
     * <code>Spanish</code><br>
     * Esta función proporciona soporte para la sintaxis
     * <a href="https://cucumber.io/docs/cucumber/api/#tag-expressions">Cucumber
     * Tags Expressions</a>
     * desde Karate y permite el uso de expresiones cucumber para filtrar pruebas en
     * ejecución desde etiquetas, por ejemplo: <code>@first and not @skip</code>
     * entre otras; esta función realiza una traducción desde Tag-Expressions v2
     * hacia Tag-Expressions v1 (soportado por karate DSL). Permite la configuración
     * de las etiquetas <code>tags</code> desde las propiedades de sistema
     * <code>System</code> y desde los runners configurados en Karate (revisar
     * documentación de Karate).
     * <br>
     * <br>
     * <i>Funcionamiento:</i>
     * Obtiene y verifica si existen tags configurados o enviados desde las
     * variables de vmArgs en Java o las propiedades de sistema
     * <code>-Dkarate.options=--tags '@test and not @skip-test'</code>, si no hay
     * tags configurados desde sistema (vmArgs o system), procede a utilizar los
     * parámetros configurados desde el runner. realiza la traducción de {@code v2}
     * a {@code v1 (soportado por Karate DSL)} y retorna los tags en el formato
     * usado en Karate DSL.
     * <br>
     * <br>
     * <code>English</code><br>
     * This function provides support for
     * <a href="https://cucumber.io/docs/cucumber/api/#tag-expressions">Cucumber
     * Tags Expressions</a> syntax from Karate and allows the use of Cucumber
     * expressions to filter running tests by tags, for example:
     * <code>@first and not @skip</code>, etc; this function performs a translation
     * from Tag-Expressions v2 to Tag-Expressions v1 (supported by the Karate DSL).
     * Allows the configuration of tags from system properties and from the runners
     * configured in Karate (see the Karate documentation).
     * <br>
     * <br>
     * 
     * Operation: Obtains and verifies if tags are configured or passed from Java's
     * vmArgs or system variables as
     * <code>-Dkarate.options=--tags '@test and not @skip-test'</code>. If no tags
     * are configured from the system (vmArgs or system), it proceeds to use the
     * parameters configured from the runner. Performs the translation from
     * {@code v2 to v1 (supported by the Karate DSL)} and returns the tags in the
     * format used in the Karate DSL.
     * <br>
     * <br>
     * 
     * <pre>
     * // for test from Karate.run
     *Karate testTags() {
     *    return Karate.run("...").tags("@second").relativeTo(getClass());
     *}
     * // for test from Runner of karate
     *public void testParallel() {
     *    Results results = Runner.path("classpath:...").tags("~@skipme").parallel(7);
     *    assertEquals(0, results.getFailCount(), results.getErrorMessages());
     *}
     * 
     * // using karate tools:
     * <code>... tags(KrtTools.configTags("@second and not @skip")) ...</code>
     * </pre>
     * 
     * <br>
     * Order of priority for tag usage:
     * <ol>
     * <li><code>System</code> properties configured from java args as
     * <code>-Dkarate.options=... --tags '@second... etc.'</code> (If it exists,
     * ignore the tags configured from Runner)</li>
     * <li><code>Tags</code> from arguments configured from runners</li>
     * </ol>
     * <br>
     * 
     * @see <a href=
     *      "https://github.com/karatelabs/karate/tree/v1.5.2?tab=readme-ov-file#parallel-execution">Karate
     *      DSL - Parallel-Execution</a>
     * @see <a href=
     *      "https://github.com/karatelabs/karate/tree/v1.5.2?tab=readme-ov-file#junit-5">Karate
     *      DSL - Junit-5</a>
     * @see <a href=
     *      "https://github.com/karatelabs/karate/tree/v1.5.2?tab=readme-ov-file#karateoptions">Karate
     *      DSL - Karateoptions</a>
     * @see <a href=
     *      "https://github.com/karatelabs/karate/tree/v1.5.2?tab=readme-ov-file#command-line">Karate
     *      DSL - Command-Line</a>
     * @see <a href=
     *      "https://cucumber.io/docs/cucumber/api/#tag-expressions">Cucumber Tags
     *      Expressions</a>
     * @see <a href=
     *      "https://behave.readthedocs.io/en/latest/tag_expressions/">Behave Tags
     *      Expressions</a>
     * @param tgs text or texts to filter tests using tags
     * @return Legible tags for karate runners or Karate DSL
     */
    public static String[] configTags(String... tgs) {
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
