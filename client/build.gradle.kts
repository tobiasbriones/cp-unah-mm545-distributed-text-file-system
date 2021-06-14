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

val platform = "win"

plugins {
    java
    id("application")
}

group = "io.github.tobiasbriones.cp"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("io.github.tobiasbriones.cp.rmifilesystem.Launcher")
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
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
