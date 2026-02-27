plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    kotlin("kapt")
}

android {
    namespace = "com.paondev.infoplat"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.paondev.infoplat"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Read from local.properties
        val jabarPajakApiUrl = project.findProperty("JABAR_PAJAK_API_URL")?.toString() ?: ""
        val jabarPajakApiKey = project.findProperty("JABAR_PAJAK_API_KEY")?.toString() ?: ""
        val jabarPajakXSignature = project.findProperty("JABAR_PAJAK_X_SIGNATURE")?.toString() ?: ""
        val jabarPajakXLocalization = project.findProperty("JABAR_PAJAK_X_LOCALIZATION")?.toString() ?: ""

        buildConfigField("String", "JABAR_PAJAK_API_URL", "\"$jabarPajakApiUrl\"")
        buildConfigField("String", "JABAR_PAJAK_API_KEY", "\"$jabarPajakApiKey\"")
        buildConfigField("String", "JABAR_PAJAK_X_SIGNATURE", "\"$jabarPajakXSignature\"")
        buildConfigField("String", "JABAR_PAJAK_X_LOCALIZATION", "\"$jabarPajakXLocalization\"")
    }

    buildTypes {
        debug {
            buildConfigField("boolean", "DEBUG_MODE", "true")
        }
        release {
            isMinifyEnabled = true
            buildConfigField("boolean", "DEBUG_MODE", "false")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

hilt {
    enableAggregatingTask = false
}

configurations.all {
    resolutionStrategy {
        force(libs.javapoet)
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.coil.compose)
    implementation(libs.navigation.compose)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.logging.interceptor)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.paging)
    implementation(libs.androidx.paging.compose)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.hilt.compiler)

    // Chucker - HTTP inspector for debug builds only
    debugImplementation(libs.chucker.library)
    releaseImplementation(libs.chucker.library.no.op)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}