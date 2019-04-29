import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

group = "com.github.monosoul"
version = "0.0.1"

plugins {
    `java-library`
    groovy
    id("org.springframework.boot") version "2.1.4.RELEASE"
    jacoco
}

apply {
    plugin("io.spring.dependency-management")
}

repositories {
    jcenter()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    val lombokDependency = "org.projectlombok:lombok:1.18.6"

    annotationProcessor(lombokDependency)
    compileOnly(lombokDependency)
    api("org.springframework:spring-context")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude("junit")
    }
    testImplementation("org.spockframework:spock-core:1.3-groovy-2.5") {
        exclude("org.codehaus.groovy")
    }
    testImplementation(localGroovy())
    testImplementation("org.apache.commons:commons-lang3:3.8.1")
}

tasks {
    withType(Test::class) {
        useJUnit()

        testLogging {
            events = setOf(PASSED, SKIPPED, FAILED)
            exceptionFormat = FULL
        }
    }

    "jacocoTestReport"(JacocoReport::class) {
        executionData(
                fileTree(project.rootDir) {
                    include("**/build/jacoco/*.exec")
                }
        )

        reports {
            xml.isEnabled = true
            xml.destination = File(buildDir, "reports/jacoco/report.xml")
            html.isEnabled = false
            csv.isEnabled = false
        }
    }
}