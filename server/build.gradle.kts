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

plugins {
    java
    id("application")
}

group = "io.github.tobiasbriones.cp"
version = "1.0-SNAPSHOT"

application {
    mainModule.set("io.github.tobiasbriones.cp.rmifilesystem.server")
    mainClass.set("io.github.tobiasbriones.cp.rmifilesystem.server.Main")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    implementation("org.hamcrest:hamcrest:2.2")
    implementation(project(":model"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<JavaCompile>().all {
    options.compilerArgs = listOf("--enable-preview")
}

tasks.withType<JavaExec>().all {
    jvmArgs("--enable-preview")
}

tasks.withType<Test>().all {
    jvmArgs("--enable-preview")
}

tasks.withType<Jar> {
    manifest {
        attributes("Main-Class" to "io.github.tobiasbriones.cp.rmifilesystem.server.Main")
    }
}
