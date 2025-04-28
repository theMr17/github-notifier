import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt)
}

android {
    namespace = "com.notifier.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.notifier.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        val properties = Properties().apply {
            val localPropertiesFile = project.rootProject.file("local.properties")
            if (localPropertiesFile.exists()) {
                load(localPropertiesFile.inputStream())
            }
        }

        debug {
            buildConfigField("String", "BASE_URL", "\"https://api.github.com/\"")
            buildConfigField(
                "String",
                "CLIENT_ID",
                "\"${properties.getProperty("CLIENT_ID", "dummy_client_id")}\""
            )
            buildConfigField(
                "String",
                "CLIENT_SECRET",
                "\"${properties.getProperty("CLIENT_SECRET", "dummy_client_secret")}\""
            )
        }
        release {
            isMinifyEnabled = false
            buildConfigField("String", "BASE_URL", "\"https://api.github.com/\"")
            buildConfigField(
                "String",
                "CLIENT_ID",
                "\"${properties.getProperty("CLIENT_ID", "dummy_client_id")}\""
            )
            buildConfigField(
                "String",
                "CLIENT_SECRET",
                "\"${properties.getProperty("CLIENT_SECRET", "dummy_client_secret")}\""
            )
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
        buildConfig = true
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.bundles.ktor)
    implementation(libs.dagger.hilt)
    ksp(libs.dagger.hilt.compiler)

    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.ktor.client.mock)
    testImplementation(libs.mockk)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
