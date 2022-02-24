plugins {
    kotlin("jvm")
}

val koinVersion = "3.1.5"

dependencies {
    implementation(kotlin("stdlib"))

    implementation(project(":raspikiln-core"))

    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("io.insert-koin:koin-ktor:$koinVersion")
}