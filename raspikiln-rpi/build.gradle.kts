plugins {
    kotlin("jvm")
}

val pi4jVersion = "2.1.1"

dependencies {
    implementation(project(":raspikiln-core"))

    implementation(kotlin("stdlib"))

    implementation("com.pi4j:pi4j-core:$pi4jVersion")
    implementation("com.pi4j:pi4j-plugin-raspberrypi:$pi4jVersion")
    implementation("com.pi4j:pi4j-plugin-pigpio:$pi4jVersion")

    implementation("io.github.microutils:kotlin-logging-jvm:2.1.21")

    implementation("org.slf4j:slf4j-api:1.7.33")
}