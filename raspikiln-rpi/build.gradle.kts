plugins {
    kotlin("jvm")
}

val pi4jVersion = "2.1.1"
val kotestVersion = "5.3.0"

dependencies {
    implementation(project(":raspikiln-core"))

    implementation(kotlin("stdlib"))

    implementation("com.pi4j:pi4j-core:$pi4jVersion")
    implementation("com.pi4j:pi4j-plugin-raspberrypi:$pi4jVersion")
    implementation("com.pi4j:pi4j-plugin-pigpio:$pi4jVersion")

    implementation("io.github.microutils:kotlin-logging-jvm:2.1.21")
    implementation("at.favre.lib:bytes:1.5.0")

    implementation("org.slf4j:slf4j-api:1.7.33")

    testImplementation("io.mockk:mockk:1.12.4")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}