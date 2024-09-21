import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
//    kotlin("jvm") version "1.7.20" // does not work with compose 1.2.0-beta02
    kotlin("jvm") version "1.7.10" // compose 1.2.0-beta02
//    kotlin("jvm") version "1.7.0"
//    kotlin("jvm") version "1.6.20" // compose 1.1.1
    // 1.6.20
    // 1.6.21
    // 1.7.0
    id("org.jetbrains.compose") version "1.2.0-beta02" // kotlin 1.7.10
//    id("org.jetbrains.compose") version "1.2.0-alpha01-dev755"
//    id("org.jetbrains.compose") version "1.1.1" // kotlin 1.6.20
    // 1.1.1 Causes button tests to fail.
}

group = "name.davidsilber"
version = "1.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

//// This is to address a problem encountered when upgrading to kotlin 1.7.0:
////                     A problem occurred configuring root project 'Theater'.
////                     > Failed to notify project evaluation listener.
////                     > 'void org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions.setUseIR(boolean)'
//// See https://github.com/JetBrains/compose-jb/issues/2108
// ... project builds with this fix, but then I couldn't execute tests
// I rolled back to kotlin 1.6.21
//allprojects {
//    configurations.all {
//        resolutionStrategy.dependencySubstitution {
//            substitute(module("org.jetbrains.compose.compiler:compiler")).apply {
//                using(module("androidx.compose.compiler:compiler:1.2.0-dev-k1.7.0-53370d83bb1"))
//            }
//        }
//    }
//}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.apache.xmlgraphics:batik-dom:1.14")
    implementation("org.apache.xmlgraphics:batik-svggen:1.14")
    implementation("com.j2html:j2html:1.5.0")
//    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.5")
//    implementation("org.jetbrains.kotlinx:kotlinx-html:0.7.5")

//    implementation( "org.jetbrains.compose.components:components-splitpane-desktop:1.0.1")
//    implementation(project(":SplitPane:library"))

    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.12.2")
    testImplementation("org.jetbrains.compose.ui:ui-test-junit4:1.0.1")
    testImplementation("org.assertj:assertj-core:3.23.1")

//    testImplementation("androidx.compose.ui:ui-test-manifest:1.0.1")
    implementation(kotlin("stdlib-jdk8"))

}

tasks.test {
    useJUnit()
    useJUnitPlatform()
    testLogging {
        events("failed","skipped","standardError")
//        events("failed","passed","skipped","standardOut","standardError")
        showCauses
        exceptionFormat = TestExceptionFormat.FULL
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "15"
}

// Todo: see this for packaging info:
// https://github.com/JetBrains/compose-jb/blob/master/tutorials/Native_distributions_and_local_execution/README.md
//
// gradle task is 'package'
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
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}