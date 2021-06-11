plugins {
    java
    id("application")
    id("org.openjfx.javafxplugin") version "0.0.9"
}

group = "io.github.tobiasbriones.cp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

javafx {
    version = "15.0.1"
    modules = listOf("javafx.controls", "javafx.fxml")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
