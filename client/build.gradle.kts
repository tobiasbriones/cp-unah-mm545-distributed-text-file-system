// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

plugins {
    java
    id("application")
    id("org.openjfx.javafxplugin") version "0.0.13"
}

version = "0.1.0"

application {
    mainModule.set("engineer.mathsoftware.cp.dtfs.client")
    mainClass.set("engineer.mathsoftware.cp.dtfs.client.Launcher")
}

javafx {
    version = "17"
    configuration = "compileOnly"
    modules("javafx.controls")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    implementation(project(":dtfs"))
    implementation(project(":impl"))
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
