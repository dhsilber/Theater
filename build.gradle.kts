import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.compose") version "1.1.0"
}

group = "name.davidsilber"
version = "1.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.apache.xmlgraphics:batik-dom:1.14")
    implementation("org.apache.xmlgraphics:batik-svggen:1.14")

//    implementation( "org.jetbrains.compose.components:components-splitpane-desktop:1.0.1")
//    implementation(project(":SplitPane:library"))

    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.12.2")
    testImplementation("org.jetbrains.compose.ui:ui-test-junit4:1.0.1")

//    testImplementation("androidx.compose.ui:ui-test-manifest:1.0.1")

}

tasks.test {
    useJUnit()
//    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "15"
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Theater"
            packageVersion = "1.0.0"
        }
    }
}