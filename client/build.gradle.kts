// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

val platform = "win"

plugins {
    java
    id("application")
}

version = "0.1.0"

application {
    mainModule.set("engineer.mathsoftware.cp.dtfs.client")
    mainClass.set("engineer.mathsoftware.cp.dtfs.client.Launcher")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    implementation("org.openjfx:javafx-base:15.0.1:${platform}")
    implementation("org.openjfx:javafx-graphics:15.0.1:${platform}")
    implementation("org.openjfx:javafx-controls:15.0.1:${platform}")
    implementation("org.openjfx:javafx-fxml:15.0.1:${platform}")

    implementation(project(":dtfs"))
    implementation(project(":impl"))
    implementation(project(":mvp"))
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
