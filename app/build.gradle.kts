
import java.util.Properties
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "tech.fourge.huddleup_frontend"
    compileSdk = 34



    defaultConfig {
        applicationId = "tech.fourge.huddleup_frontend"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val properties = Properties()
        val localPropertiesFile = project.rootProject.file("local.properties")

        if (localPropertiesFile.exists()) {
            localPropertiesFile.inputStream().use { properties.load(it) }
        }

        val googleClientId = properties.getProperty("GOOGLE_CLIENT_ID") ?: throw GradleException("GOOGLE_CLIENT_ID is missing in local.properties")

        // Add GOOGLE_CLIENT_ID to BuildConfig
        buildConfigField("String", "GOOGLE_CLIENT_ID", "\"$googleClientId\"")
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
        viewBinding = true
        dataBinding = true
        buildConfig = true

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation ("com.google.android.gms:play-services-base:18.5.0")
    implementation ("com.google.android.gms:play-services-auth:21.2.0")
    implementation(libs.androidx.material3.android)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidx.biometric.ktx)
    implementation(libs.androidx.security.crypto.ktx)
    testImplementation ("junit:junit:4.13.2")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.2")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.ui.test.android)
    implementation(libs.firebase.functions.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    implementation ("androidx.biometric:biometric:1.2.0-alpha05")
    implementation ("androidx.security:security-crypto:1.1.0-alpha05")
}