Execution de recettes OpenRewrite directement dans un projet


Execution de recettes OpenRewrite sans modification du projet
## JUnit 4 to JUnit 5 Migration

https://docs.openrewrite.org/recipes/java/testing/junit5/junit4to5migration

```bash
mvn -Drewrite.recipeArtifactCoordinates="org.openrewrite.recipe:rewrite-testing-frameworks:3.14.1" -Drewrite.activeRecipes="org.openrewrite.java.testing.junit5.JUnit4to5Migration" org.openrewrite.maven:rewrite-maven-plugin:dryRun
```

## Dans un contexte springboot : Unnecessary Spring @RunWith

https://docs.openrewrite.org/recipes/java/spring/boot2/springboot2junit4to5migration

```bash
mvn -Drewrite.recipeArtifactCoordinates="org.openrewrite.recipe:rewrite-testing-frameworks:3.14.1,org.openrewrite.recipe:rewrite-spring:6.11.1" -Drewrite.activeRecipes="org.openrewrite.java.testing.junit5.JUnit4to5Migration,org.openrewrite.java.spring.boot2.UnnecessarySpringRunWith" org.openrewrite.maven:rewrite-maven-plugin:dryRun
```