plugins {
    `kotlin-dsl`
    id("maven-publish")
}

group = "dev.chainmail"
version = "release"

repositories {
    mavenCentral()
    google()
    jcenter()
}

dependencies {

    compileOnly(gradleApi())

    implementation("de.undercouch:gradle-download-task:4.1.1")
    implementation("com.android.tools.build:gradle:4.2.0-beta02")

}

gradlePlugin {
    plugins {
        create("dragapult") {
            id = "dragapult.gradle"
            implementationClass = "dev.chainmail.dragapult.gradle.DragapultPlugin"
        }
    }
}