plugins {
    kotlin("jvm")
}

val jacksonVersion = "2.12.2"
val kotestVersion = "5.1.0"

dependencies {
    implementation(kotlin("stdlib"))

    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")

    api(project(":raspikiln-tsdb"))

    api("org.apache.commons:commons-lang3:3.12.0")
    api("commons-io:commons-io:2.11.0")

    api("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    api("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion")
    api("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

    api("io.github.microutils:kotlin-logging-jvm:2.1.21")

    api("org.slf4j:slf4j-api:1.7.33")

    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")

}

tasks.withType<Test> {
    useJUnitPlatform()
}