@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id(libs.plugins.android.application.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.kotlin.kapt.get().pluginId)
    id(libs.plugins.hilt.android.get().pluginId)
    id(libs.plugins.google.services.get().pluginId)
    id(libs.plugins.android.library.get().pluginId) apply false
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.colledk.colingo"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true

        buildConfigField(
            "String",
            "OPENAI_API_KEY",
            "\"${project.findProperty("OPENAI_API_KEY")}\""
        )
    }

    kotlin {
        jvmToolchain(libs.versions.jvm.get().toInt())
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Androidx
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.splashscreen)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.datastore)

    // Internal modules
    implementation(project(":onboarding"))
    implementation(project(":theme"))
    implementation(project(":chat"))
    implementation(project(":profile"))
    implementation(project(":community"))
    implementation(project(":home"))

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.junit.ktx)
    implementation(libs.androidx.monitor)
    implementation(libs.androidx.material3.adaptive.navigation.suite.android)
    kapt(libs.hilt.compiler)

    // Timber
    implementation(libs.timber)

    // Coil
    implementation(libs.coil)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)

    // Reflection
    implementation(libs.reflection)

    // Open AI
    implementation(platform(libs.openai.bom))
    implementation(libs.openai.client)
    runtimeOnly(libs.openai.okhttp)

    // Test
    testImplementation(libs.bundles.testing.unit)
}