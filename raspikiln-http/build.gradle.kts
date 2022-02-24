plugins {
    kotlin("jvm")
}

val koinVersion = "3.1.5"
val ktorVersion = "1.6.7"

dependencies {
    implementation(kotlin("stdlib"))

    implementation(project(":raspikiln-core"))
    implementation(project(":raspikiln-controller"))

    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("io.insert-koin:koin-ktor:$koinVersion")

    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-jackson:$ktorVersion")
}