plugins {
    application
    kotlin("jvm")
}

application {
    mainClass.set("org.raspikiln.server.ServerKt")
    applicationDefaultJvmArgs = listOf("-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005")
}

val cliktVersion = "3.4.2"
val koinVersion = "3.2.0"
val log4j2 = "2.17.2"
val ktorVersion = "2.0.1"

dependencies {
    implementation(kotlin("stdlib"))

    implementation(project(":raspikiln-core"))
    implementation(project(":raspikiln-http"))
    implementation(project(":raspikiln-rpi"))

    implementation("com.github.ajalt.clikt:clikt:$cliktVersion")

    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("io.insert-koin:koin-ktor:$koinVersion")

    implementation("io.github.microutils:kotlin-logging-jvm:2.1.21")

    implementation("org.apache.logging.log4j:log4j-api:$log4j2")
    implementation("org.apache.logging.log4j:log4j-core:$log4j2")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:$log4j2")

    implementation("org.slf4j:slf4j-api:1.7.36")

    testImplementation(project(":raspikiln-mock"))
    testImplementation("io.ktor:ktor-server-html-builder:$ktorVersion")
}