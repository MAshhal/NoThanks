plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.mystic.nothanks"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mystic.nothanks"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            val signAsDebug: String? by project
            signingConfig =
                signingConfigs.getByName(if (signAsDebug.toBoolean()) "debug" else "release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

ksp {
    arg("room.generateKotlin", "true")
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.iconsExtended)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.testManifest)

    // Compose Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.hilt.ext.navigation)

    // Barcode Scanning

    // Hilt Implementation
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    ksp(libs.hilt.ext.compiler)

    // Room Implementation
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)

    // Datastore Implementation
    implementation(libs.androidx.dataStore.preferences)

    // KotlinX
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.gson)

    // Various Dependencies
    // Pretty Logger
    implementation(libs.logger)
    // Oss plugin

    implementation(libs.coil.kt.compose)
    implementation("androidx.palette:palette-ktx:1.0.0")

    implementation("com.google.android.gms:play-services-code-scanner:16.1.0")

    implementation("com.google.android.gms:play-services-ads:23.2.0")

    implementation("androidx.camera:camera-viewfinder-compose:1.0.0-alpha02")

    implementation("io.github.mr0xf00:easycrop:0.1.1")
}