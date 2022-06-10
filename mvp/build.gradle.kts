// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

val platform = "win"

plugins {
    java
}

version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    implementation("org.openjfx:javafx-base:15.0.1:${platform}")
    implementation("org.openjfx:javafx-graphics:15.0.1:${platform}")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
