import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    kotlin("kapt")
}

/* ===========================
   Load local.properties
   =========================== */
val properties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { properties.load(it) }
} else {
    println("WARNING: local.properties not found!")
}

fun getProps(propName: String): String {
    return properties.getProperty(propName) ?: System.getenv(propName) ?: ""
}

android {
    namespace = "com.paondev.infoplat"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.paondev.infoplat"
        minSdk = 23
        targetSdk = 36
        versionCode = 2
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Read from local.properties
        val jabarPajakApiUrl = getProps("JABAR_PAJAK_API_URL")
        val jabarPajakApiKey = getProps("JABAR_PAJAK_API_KEY")
        val jabarPajakXSignature = getProps("JABAR_PAJAK_X_SIGNATURE")
        val jabarPajakXLocalization = getProps("JABAR_PAJAK_X_LOCALIZATION")
        val JatimCaptchaUrl = getProps("JATIM_CAPTCHA_URL")
        val JatimPajakApiUrl = getProps("JATIM_PAJAK_API_URL")
        val DiypPajakApiUrl = getProps("DIYP_PAJAK_API_URL")
        val apiUrlInfoPlat = getProps("API_URL_INFO_PLAT")
        val apiUrlInfoPlatOcr = getProps("API_URL_INFO_PLAT_OCR")

        buildConfigField("String", "JABAR_PAJAK_API_URL", "\"$jabarPajakApiUrl\"")
        buildConfigField("String", "JABAR_PAJAK_API_KEY", "\"$jabarPajakApiKey\"")
        buildConfigField("String", "JABAR_PAJAK_X_SIGNATURE", "\"$jabarPajakXSignature\"")
        buildConfigField("String", "JABAR_PAJAK_X_LOCALIZATION", "\"$jabarPajakXLocalization\"")

        buildConfigField("String", "JATIM_CAPTCHA_URL", "\"$JatimCaptchaUrl\"")
        buildConfigField("String", "JATIM_PAJAK_API_URL", "\"$JatimPajakApiUrl\"")
        buildConfigField("String", "DIYP_PAJAK_API_URL", "\"$DiypPajakApiUrl\"")
        buildConfigField("String", "API_URL_INFO_PLAT", "\"$apiUrlInfoPlat\"")
        buildConfigField("String", "API_URL_INFO_PLAT_OCR", "\"$apiUrlInfoPlatOcr\"")
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