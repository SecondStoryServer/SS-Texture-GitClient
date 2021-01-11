import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    application
}

group = "com.github.syari.ss.texture.gitclient"

repositories {
    mavenCentral()
    maven(url = "https://dl.bintray.com/sya-ri/maven/")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.github.syari:kgit:1.0.0")
    implementation("org.slf4j:slf4j-log4j12:1.7.21")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<ShadowJar> {
    baseName = "git"
    classifier = null
    destinationDirectory.set(file("../"))
}

application {
    mainClassName = "$group.MainKt"
}