/*
 * This file is a part of the Chameleon Framework, licensed under the MIT License.
 *
 * Copyright (c) 2021-2023 The Chameleon Framework Authors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
import com.adarshr.gradle.testlogger.theme.ThemeType
import net.ltgt.gradle.errorprone.errorprone
import net.ltgt.gradle.nullaway.nullaway
import org.apache.tools.ant.filters.ReplaceTokens
import java.time.Instant

plugins {
    id("net.kyori.indra")
    id("net.kyori.indra.git")
    id("net.kyori.indra.checkstyle")
    id("net.kyori.indra.licenser.spotless")

    id("com.adarshr.test-logger")
    id("net.ltgt.errorprone")
    id("net.ltgt.nullaway")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
val requires17 = setOf(
    "chameleon-example",
    "chameleon-platform-folia",
    "chameleon-platform-sponge",
)

indra {
    checkstyle(libs.findVersion("checkstyle").get().toString())
    javaVersions {
        if (project.name in requires17) {
            target(17)
            testWith(17)
        } else {
            target(11)
            testWith(11, 17)
        }
    }
}

indraSpotlessLicenser {
    property("year", "2021-2023")
}

testlogger {
    theme = ThemeType.MOCHA_PARALLEL
}

dependencies {
    errorprone(libs.findLibrary("build-errorprone-core").get())
    errorprone(libs.findLibrary("build-nullaway-core").get())
    compileOnly(libs.findLibrary("build-errorprone-annotations").get())
}

configurations {
    testCompileClasspath {
        exclude(group = "junit")
    }
}

tasks.create<Sync>("processSources") {
    from(java.sourceSets["main"].java)
    filter(ReplaceTokens::class, mapOf("tokens" to mapOf(
        "version" to rootProject.version,
        "gitBranch" to (indraGit.branchName() ?: "unknown"),
        "gitCommitHash" to (indraGit.commit()?.name ?: "unknown"),
        "buildTime" to Instant.now().toString(),
    )))
    into(layout.buildDirectory.dir("src"))
}

tasks.compileJava {
    dependsOn(tasks.getByName("processSources"))
    source = tasks.getByName("processSources").outputs.files.asFileTree
}

tasks.withType<JavaCompile>().configureEach {
    options.errorprone {
        disable("AnnotateFormatMethod")
        disable("CanIgnoreReturnValueSuggester")

        nullaway {
            error()
            annotatedPackages.add("dev.hypera.chameleon")
            unannotatedSubPackages.add("dev.hypera.chameleon.example")
            acknowledgeRestrictiveAnnotations.set(true)
            checkOptionalEmptiness.set(true)
            checkContracts.set(true)
        }
    }
}
