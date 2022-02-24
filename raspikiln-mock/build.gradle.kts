plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation(project(":raspikiln-core"))

    implementation("io.github.microutils:kotlin-logging-jvm:2.1.21")

    implementation("org.slf4j:slf4j-api:1.7.33")
}