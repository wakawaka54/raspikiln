plugins {
    kotlin("jvm")
}

group = "org.raspikiln"
version = "1.0"

repositories {
    mavenCentral()
}

val jacksonVersion = "2.12.2"
val kotestVersion = "5.1.0"


dependencies {
    implementation(kotlin("stdlib"))
    api("org.msgpack:msgpack-core:0.9.0")

    api("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    api("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
}

tasks.withType<Test> {
    useJUnitPlatform()
}