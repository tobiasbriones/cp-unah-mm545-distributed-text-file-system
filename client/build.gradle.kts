/*
 * Copyright (c) 2021 Tobias Briones. All rights reserved.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 *
 * This file is part of Course Project at UNAH-MM545: Distributed Text File
 * System.
 *
 * This source code is licensed under the BSD-3-Clause License found in the
 * LICENSE file in the root directory of this source tree or at
 * https://opensource.org/licenses/BSD-3-Clause.
 */

val mainModuleValue = "io.github.tobiasbriones.cp.rmifilesystem.client"
val mainClassValue = "${mainModuleValue}.Launcher"
val platformValue = "win"

version = "1.0.0"

plugins {
    java
    id("application")
    id("org.openjfx.javafxplugin") version "0.0.10"
}

application {
    mainModule.set(mainModuleValue)
    mainClass.set(mainClassValue)
}

javafx {
    version = "16"
    modules = listOf("javafx.controls")
}

dependencies {
    implementation(project(":model"))
    implementation(project(":impl"))
    implementation(project(":mvp"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

repositories {
    mavenCentral()
}

tasks {
    test {
        useJUnitPlatform()
    }

    jar {
        manifest {
            attributes(
                "Main-Class" to mainClassValue
            )
        }
    }

    withType<JavaCompile>().all {
        options.compilerArgs = listOf("--enable-preview")
    }

    withType<JavaExec>().all {
        jvmArgs("--enable-preview")
    }

    withType<Test>().all {
        jvmArgs("--enable-preview")
    }

    register<Copy>("copyLicenses") {
        from(".") {
            include("LICENSE*")
        }
        into("src/main/resources")
    }

    register<Copy>("copyLibs") {
        dependsOn("installDist")
        from("${buildDir}/install/${project.name}/lib")
        into("${buildDir}/libs")
    }

    register<Copy>("copyModules") {
        dependsOn("installDist")
        from("${buildDir}/install/${project.name}/lib") {
            include(
                "javafx-*-win*",
                "client-*",
                "impl-*",
                "model-*",
                "mvp-*"
            )
        }
        into("${buildDir}/modules")
    }

    register<Task>("buildRelease") {
        dependsOn("clean", "copyLibs", "copyModules")
        doLast {
            exec {
                delete("${buildDir}/release")
                commandLine(
                    "${org.gradle.internal.jvm.Jvm.current().javaHome}/bin/jpackage",
                    "--type",
                    "app-image",
                    "--input",
                    "${buildDir}/libs",
                    "--main-jar",
                    "client-${version}.jar",
                    "--main-class",
                    mainClassValue,
                    "--module-path",
                    "${buildDir}/modules",
                    "--add-modules",
                    "java.base,java.datatransfer,java.desktop,java.logging,java.management,java.naming,java.net.http,java.scripting,java.sql,java.transaction.xa,java.xml,"
                        + "javafx.base,javafx.controls,javafx.graphics",
                    "--dest",
                    "${buildDir}/release",
                    "--java-options",
                    "--enable-preview",
                    "--name",
                    "Tobi",
                    "--app-version",
                    version.toString(),
                    "--description",
                    "Desc",
                    "--icon",
                    "src/main/resources/ic_folder.png",
                    "--vendor",
                    "TB",
                    "--copyright",
                    "Copyright (c) 2021 Tobias Briones. All rights reserved.",
                    "--verbose"
                )
            }
            delete {
                //delete("build/release/Mobitra/Mobitra.ico", "build/release/Mobitra/.jpackage.xml")
            }
            copy {
                from(".") {
                    include("LICENSE*")
                }
                into("${buildDir}/release/Tobi")
            }
        }
    }

    register<Zip>("zipRelease") {
        val archive = "Tobi-${project.version}.zip"
        archiveFileName.set(archive)
        destinationDirectory.set(File("${buildDir}/release"))
        from("${buildDir}/release") {
            include("Tobi/**")
        }
    }

    register<Task>("release") {
        dependsOn("buildRelease", "zipRelease")
    }
}
