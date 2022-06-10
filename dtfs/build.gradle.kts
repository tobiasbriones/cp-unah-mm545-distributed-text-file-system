plugins { java }

version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    implementation("org.hamcrest:hamcrest:2.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<JavaCompile>().all {
    options.compilerArgs = listOf("--enable-preview")
}

tasks.withType<Test>().all {
    jvmArgs("--enable-preview")
}