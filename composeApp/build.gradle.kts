import com.android.build.gradle.internal.tasks.factory.dependsOn
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(project.projectDir.path)
                    }
                }
            }
        }
        binaries.executable()
    }
    
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    jvm("desktop")
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        val desktopMain by getting
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            
            // Coroutines
            implementation(libs.kotlinx.coroutines.core)
//            runtimeOnly(libs.kotlinx.coroutines.swing)
            
            implementation(libs.kotlinx.datetime)
            
            implementation(compose.materialIconsExtended)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }

        wasmJsMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-wasm-js:1.9.0-RC")
        }
    }
}

android {
    namespace = "com.rokoblak.kmpdatepicker"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.rokoblak.kmpdatepicker"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    dependencies {
        debugImplementation(compose.uiTooling)
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.rokoblak.kmpdatepicker"
            packageVersion = "1.0.0"
        }
    }
}

val versionTextFile = "version.txt"
val packageName = "com.rokoblak.kmpdatepicker"

tasks.register("generateBuildConfig") {

    val versionFile = File(versionTextFile)
    if (!versionFile.exists()) {
        throw GradleException("Version file does not exist.")
    }

    val (major, minor, patch) = versionFile.readVersion()
    val formattedVersion = "$major.$minor.$patch"
    doLast {
        val fileContent = """
            package $packageName.config
            
            // Autogenerated, do not modify
            object AppBuildConfig {
                const val VERSION = "$formattedVersion"
            }
        """.trimIndent()

        val buildDir = layout.buildDirectory.get().asFile
        val commonMainDir = file("${buildDir}/generated/kotlin/config/")
        commonMainDir.mkdirs()
        val generatedFile = commonMainDir.resolve("AppBuildConfig.kt")
        generatedFile.delete()
        generatedFile.writeText(fileContent)
        val success = generatedFile.setReadOnly()
        if (!success) {
            logger.warn("Failed to set the file as read-only: ${generatedFile.absolutePath}")
        }
    }
}

kotlin.sourceSets.getByName("commonMain") {
    val buildDir = layout.buildDirectory.get().asFile
    kotlin.srcDir("${buildDir}/generated/kotlin/config")
}

kotlin.targets.all {
    compilations.all {
        compileTaskProvider.dependsOn("generateBuildConfig")
    }
}

val incrementVersion = tasks.register("incrementVersion") {
    val versionFile = File(versionTextFile)
    doLast {
        if (!versionFile.exists()) {
            throw GradleException("Version file does not exist.")
        }
        val (major, minor, patch) = versionFile.readVersion()

        val newVersion = "$major.$minor.${patch + 1}"

        versionFile.writeText(newVersion)

        println("Version updated to $newVersion")
    }
}

fun File.readVersion(): List<Int> {
    val currentVersion = readText().trim()
    return currentVersion.split('.').map { it.toInt() }
}

// Prepare the wasm distribution and copy it over to the docs folder
// Run via ./gradlew prepareWasmPage
tasks.register<Copy>("prepareWasmPage") {
    dependsOn("wasmJsBrowserDistribution")
    dependsOn(incrementVersion)
    from("$rootDir/composeApp/build/dist/wasmJs/productionExecutable")
    into("$rootDir/docs")
    doFirst {
        val targetDir = file("$rootDir/docs")
        if (targetDir.exists()) {
            delete(targetDir)
        }
        mkdir(targetDir)
    }
}
