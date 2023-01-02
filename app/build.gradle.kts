@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.flaviofaria.catalog")
}

android {
    namespace = "com.arnyminerz.wallet"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.arnyminerz.wallet"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "ACCOUNT_TYPE", "\"firefly\"")
        resValue("string", "ACCOUNT_TYPE", "firefly")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    ksp {
        arg("room.schemaLocation", "$projectDir/schemas".toString())
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
    catalog {
        generateResourcesExtensions = false
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    implementation("androidx.activity:activity-compose:1.6.1")

    // Material Library (Used for theming)
    implementation("com.google.android.material:material:1.7.0")

    // Jetpack Compose - Base
    val composeBom = platform("androidx.compose:compose-bom:2022.11.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.runtime:runtime-livedata")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")

    // Jetpack Compose - Accompanist
    val accompanistVersion = "0.27.1"
    implementation("com.google.accompanist:accompanist-pager:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-navigation-animation:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-permissions:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-systemuicontroller:$accompanistVersion")

    // Jetpack Compose - Dialogs
    val dialogsVersion = "1.0.1"
    implementation("com.maxkeppeler.sheets-compose-dialogs:core:$dialogsVersion")
    implementation("com.maxkeppeler.sheets-compose-dialogs:calendar:$dialogsVersion")

    // For loading images
    implementation("io.coil-kt:coil-compose:2.2.2")

    // For decompressing zip files
    implementation("org.apache.commons:commons-compress:1.22")

    // For generating barcodes
    implementation("com.google.zxing:core:3.5.1")

    // Timber logger
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Preferences storage
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // OkHttp for web requests
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.10.0"))
    implementation("com.squareup.okhttp3:okhttp")

    // ML for QR code scanning
    implementation("com.google.mlkit:barcode-scanning:17.0.2")

    // Custom Tabs
    implementation("androidx.browser:browser:1.4.0")

    // CameraX
    val cameraxVersion = "1.1.0"
    implementation("androidx.camera:camera-camera2:$cameraxVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraxVersion")
    implementation("androidx.camera:camera-view:$cameraxVersion")
    implementation("androidx.camera:camera-extensions:$cameraxVersion")

    // Jetpack Room
    val roomVersion = "2.4.3"
    implementation("androidx.room:room-runtime:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // Note: for versions see https://developer.android.com/studio/write/java8-support#library-desugaring-versions
    @Suppress("GradleDependency")
    //noinspection GradleDependency
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.2.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.4")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}