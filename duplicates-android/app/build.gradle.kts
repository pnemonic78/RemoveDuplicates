plugins {
    id("com.android.application")
    id("com.google.devtools.ksp") version "1.8.22-1.0.11"
    kotlin("android")

    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

val versionMajor = (project.properties["APP_VERSION_MAJOR"] as String).toInt()
val versionMinor = (project.properties["APP_VERSION_MINOR"] as String).toInt()

android {
    compileSdk = BuildVersions.compileSdkVersion

    defaultConfig {
        applicationId = "com.github.android.removeduplicates"
        minSdk = BuildVersions.minSdkVersion
        targetSdk = BuildVersions.targetSdkVersion
        versionCode = versionMajor * 100 + versionMinor
        versionName = "${versionMajor}." + versionMinor.toString().padStart(2, '0')
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables.useSupportLibrary = true

        buildConfigField("Boolean", "FEATURE_CALL_LOGS", "true")
        buildConfigField("Boolean", "FEATURE_SMS", "true")
        resConfigs(
            "ar",
            "cs",
            "de",
            "el",
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
    implementation("androidx.room:room-common:${BuildVersions.roomVersion}")
    implementation("androidx.room:room-runtime:${BuildVersions.roomVersion}")
    implementation("androidx.room:room-ktx:${BuildVersions.roomVersion}")
    ksp("androidx.room:room-compiler:${BuildVersions.roomVersion}")

    // Logging
    implementation("com.google.firebase:firebase-crashlytics:18.0.1")

    // Testing
    testImplementation("junit:junit:${BuildVersions.junitVersion}")
}
