import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    kotlin("jvm") version "1.4.21"
    id("org.jlleitschuh.gradle.ktlint") version "9.4.1"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "com.github.syari.ss.texture.gitclient"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.github.sya-ri:kgit:1.0.3")
    implementation("org.slf4j:slf4j-log4j12:2.0.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<ShadowJar> {
    archiveBaseName.set("git")
    archiveClassifier.set("")
    destinationDirectory.set(file("../"))
    manifest {
        attributes["Main-Class"] = "${project.group}.MainKt"
    }
}

configure<KtlintExtension> {
    version.set("0.40.0")
}
