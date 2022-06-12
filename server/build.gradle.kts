// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

version = "0.1.0"

plugins {
    java
    id("application")
}

application {
    mainModule.set("engineer.mathsoftware.cp.dtfs.server")
    mainClass.set("engineer.mathsoftware.cp.dtfs.server.Main")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    implementation("org.hamcrest:hamcrest:2.2")
    implementation(project(":dtfs"))
    implementation(project(":impl"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<JavaCompile>()
    .all {
        options.compilerArgs = listOf("--enable-preview")
    }

tasks.withType<JavaExec>()
    .all {
        jvmArgs("--enable-preview")
    }

tasks.withType<Test>()
    .all {
        jvmArgs("--enable-preview")
    }

tasks.withType<Jar> {
    manifest {
        attributes("Main-Class" to "engineer.mathsoftware.cp.dtfs.server.Main")
    }
}
