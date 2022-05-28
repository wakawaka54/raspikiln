import com.github.gradle.node.npm.task.NpxTask

plugins {
    application
    kotlin("jvm")
    id("com.github.node-gradle.node") version "3.2.1"
}

application {
    mainClass.set("org.raspikiln.dashboard.ServerKt")
    applicationDefaultJvmArgs = listOf("-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5004")
}

group = "org.raspikiln"
version = "1.0"

repositories {
    mavenCentral()
}

sourceSets {
    main {
        resources {
            srcDir("webapp/dist")
            include("**")
        }
    }
}

node {
    download.set(false)
    nodeProjectDir.set(file("webapp"))
}

tasks {
    val buildWebApp by registering(NpxTask::class) {
        dependsOn(npmInstall)
        command.set("ng")
        args.addAll("build")
    }

    build {
        dependsOn(buildWebApp)
    }
}

val log4j2 = "2.17.2"
val ktorVersion = "2.0.1"

dependencies {
    implementation(kotlin("stdlib"))

    implementation("io.github.microutils:kotlin-logging-jvm:2.1.21")

    implementation("org.apache.logging.log4j:log4j-api:$log4j2")
    implementation("org.apache.logging.log4j:log4j-core:$log4j2")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:$log4j2")

    implementation("org.slf4j:slf4j-api:1.7.36")

    api("io.ktor:ktor-server-core:$ktorVersion")
    api("io.ktor:ktor-server-netty:$ktorVersion")
}