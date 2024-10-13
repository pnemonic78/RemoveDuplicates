plugins {
    id("com.android.application")
    id("com.google.devtools.ksp") version "2.0.20-1.0.25"
    kotlin("android")

    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

val versionMajor = (project.properties["APP_VERSION_MAJOR"] as String).toInt()
val versionMinor = (project.properties["APP_VERSION_MINOR"] as String).toInt()

android {
    namespace = "com.github.android.removeduplicates"
    compileSdk = BuildVersions.compileSdk

    defaultConfig {
        applicationId = "com.github.android.removeduplicates"
        minSdk = BuildVersions.minSdk
        targetSdk = BuildVersions.targetSdk
        versionCode = versionMajor * 100 + versionMinor
        versionName = "${versionMajor}." + versionMinor.toString().padStart(2, '0')
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables.useSupportLibrary = true

        buildConfigField("Boolean", "FEATURE_CALL_LOGS", "true")
        buildConfigField("Boolean", "FEATURE_SMS", "true")
        val locales = listOf(
            "ar",
            "cs",
            "de",
            "el",
            "en",
            "es",
            "fi",
            "fr",
            "hi",
            "it",
            "iw",
            "ja",
            "ko",
            "nl",
            "pl",
            "pt",
            "ru",
            "vi",
            "zh"
        )
        resourceConfigurations += locales
    }

    bundle {
        language {
            // Specifies that the app bundle should not support
            // configuration APKs for language resources. These
            // resources are instead packaged with each base and
            // dynamic feature APK.
            enableSplit = false
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file("../release.keystore")
            storePassword = project.properties["STORE_PASSWORD_RELEASE"] as String
            keyAlias = "release"
            keyPassword = project.properties["KEY_PASSWORD_RELEASE"] as String
        }
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
        }
        getByName("release") {
            proguardFiles(getDefaultProguardFile("proguard-android.txt"))
            proguardFiles("proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    lint {
        disable += "AllowBackup"
        disable += "GoogleAppIndexingWarning"
        disable += "InconsistentLayout"
        disable += "LocaleFolder"
        disable += "UnusedAttribute"
        disable += "UnusedResources"
    }

    flavorDimensions += "privacy"

    productFlavors {
        create("google") {
            dimension = "privacy"
            buildConfigField("Boolean", "FEATURE_CALL_LOGS", "false")
            buildConfigField("Boolean", "FEATURE_SMS", "false")
        }
        create("regular") {
            dimension = "privacy"
            isDefault = true
        }
    }
}

dependencies {
    implementation(project(":android-lib:lib"))

    // Views
    implementation("androidx.cardview:cardview:1.0.0")

    // Database
    implementation("androidx.room:room-common:${BuildVersions.room}")
    implementation("androidx.room:room-runtime:${BuildVersions.room}")
    implementation("androidx.room:room-ktx:${BuildVersions.room}")
    ksp("androidx.room:room-compiler:${BuildVersions.room}")

    // Logging
    implementation("com.google.firebase:firebase-crashlytics:19.2.0")

    // Testing
    testImplementation("junit:junit:${BuildVersions.junit}")
}
