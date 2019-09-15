import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES

group = "com.github.monosoul"
version = "0.0.1"

plugins {
    `java-library`
    groovy
    id("org.springframework.boot") version "2.1.4.RELEASE" apply false
    jacoco
    `maven-publish`
    signing
}

apply {
    plugin("io.spring.dependency-management")
}

the<DependencyManagementExtension>().apply {
    imports {
        mavenBom(BOM_COORDINATES)
    }
}

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    val lombokDependency = "org.projectlombok:lombok:1.18.6"
    val spockVersion = "1.3-groovy-2.5"

    annotationProcessor(lombokDependency)
    compileOnly(lombokDependency)
    compileOnly("org.springframework:spring-context")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude("junit")
    }
    testImplementation("org.spockframework:spock-core:$spockVersion") {
        exclude("org.codehaus.groovy")
    }
    testImplementation("org.spockframework:spock-spring:$spockVersion")
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

val javadocJar by tasks.registering(Jar::class) {
    classifier = "javadoc"
    from(tasks.javadoc)
}

val sourcesJar by tasks.registering(Jar::class) {
    classifier = "sources"
    from(sourceSets.main.get().allSource)
}

artifacts {
    archives(javadocJar)
    archives(sourcesJar)
}

publishing {
    publications {
        create<MavenPublication>("mavenCentral") {
            artifact(tasks.jar.get())
            artifact(javadocJar.get())
            artifact(sourcesJar.get())

            pom {
                name.set("Spring OrderConfig")
                description.set("A library that provides a convenient way of configuring decorators order in Spring.")
                url.set("https://github.com/monosoul/spring-orderconfig")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("monosoul")
                        name.set("Andrei Nevedomskii")
                        email.set("Kloz.Klaud@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/monosoul/spring-orderconfig.git")
                    developerConnection.set("scm:git:ssh://github.com/monosoul/spring-orderconfig.git")
                    url.set("https://github.com/monosoul/spring-orderconfig")
                }
            }
        }
    }
    repositories {
        maven {
            setUrl("https://oss.sonatype.org/service/local/staging/deploy/maven2")
            credentials {
                val nexusUsername: String by project
                val nexusPassword: String by project
                username = nexusUsername
                password = nexusPassword
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenCentral"])
}