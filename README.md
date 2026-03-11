# krt-tools

Tools for automated testing using Karate DSL.

This repository contains utilities and tools developed in Java to facilitate, extend, or simplify test automation with [Karate DSL](https://github.com/karatelabs/karate), a popular platform for API testing, services, and E2E test automation.

## Features

- Scripts and utilities to integrate, extend, or facilitate testing using Karate.
- Designed for developers and testers who use Karate within their QA or CI/CD pipelines.

## Prerequisites

- Java JDK 8 or higher
- Maven 3.x

## How to use

### Tags configuration for Karate

Only with the implementation of Karate and test runners, you should know the syntax of Tag-expression-v1.
```java
/* ussing Karate.run */
...
    @Karate.Test
    Karate testTags() {
        return Karate.run("tags")
                /* OR boolean expression */
                .tags("@second,@first,@happy")
                /* AND boolean expression */
                .tags("@second","@data")
                /* NOT boolean expression */
                .tags("~@todo")
                
                .relativeTo(getClass());
    }
/* ussing Runner of Karate */
...
    @Test
    void testParallel() {
        Results results = Runner.path("classpath:animals")
                /* OR boolean expression */
                .tags("@second,@first,@happy")
                /* AND boolean expression */
                .tags("@messages","@apis","@replt")
                /* NOT boolean expression */
                .tags("~@skipme")
                
                .parallel(5);
        assertEquals(0, results.getFailCount(), results.getErrorMessages());
    }
```
With the help of the `krt-tools` you can use logical operators (`or`,`and` and `not`) and their combinations, this can be used in `Runner` of Karate core or `Karate.run` implementations.
```java
// import class
import io.github.sware.tools.KrtTools;
...

/* OR boolean expression */
.tags(KrtTools.configTags("@second or @first or @happy"))
/* AND boolean expression */
.tags(KrtTools.configTags("@messages and @apis and @replt"))
/* NOT boolean expression */
.tags(KrtTools.configTags("not @skipme"))
```
Additionally, the use of this method provides support for system options or command arguments (*).

> \* Using the system properties overwrites or ignores the configuration from the test runner.

From command lines or bash execution

```bash
# --tags option or -t abreviate, or combinations
mvn test "-Dkarate.options= --tags @test-runnable-01 and @char-test or not @wip"
mvn test "-Dkarate.options= -t @commons or @api-01"
```

or configured from vmArgs

```yml
-Dkarate.options= --tags @test-runnable-01 and @char-test or not @wip -t=@testing -t@run

-Dkarate.options= -t @test-runnable-01 or @run
```

## Contributions

Contributions are welcome! Please open an Issue or Pull Request for suggestions, improvements, or corrections.


## 🔗 Helpful resources

- [Karate Framework](https://github.com/karatelabs/karate)
- [Official Karate Documentation](https://github.com/karatelabs/karate#documentation)
