plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

kapt {
    correctErrorTypes = true
}

android {
    namespace = "com.example.appnote"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.appnote"
        minSdk = 23
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        kotlinCompilerExtensionVersion = "1.4.7"
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    packaging.resources {
        excludes += "/META-INF/AL2.0"
        excludes += "/META-INF/LGPL2.1"
    }
}

dependencies {

    // Core dependencies
    implementation("androidx.core:core-ktx:1.10.1")

    // Compose dependencies
    val composeBom = platform("androidx.compose:compose-bom:2023.05.01")
    implementation(composeBom)

    implementation("androidx.compose.material3:material3")

    implementation("androidx.compose.ui:ui-text-google-fonts")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    implementation("androidx.activity:activity-compose")
    implementation("androidx.compose.runtime:runtime-livedata")

    // Architecture dependencies
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")

    implementation("androidx.navigation:navigation-compose:2.5.3")

    implementation("androidx.room:room-runtime:2.5.1")
    implementation("androidx.room:room-ktx:2.5.1")
    kapt("androidx.room:room-compiler:2.5.1")

    implementation("com.google.dagger:hilt-android:2.45")
    kapt("com.google.dagger:hilt-android-compiler:2.45")

    //  Unit test dependencies
    testImplementation("junit:junit:4.13.2")

    testImplementation("androidx.test:core-ktx:1.5.0")
    testImplementation("androidx.test.ext:junit-ktx:1.1.5")
    testImplementation("androidx.test.espresso:espresso-core:3.5.1")
    testImplementation("androidx.test.espresso:espresso-contrib:3.5.1")

    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("androidx.navigation:navigation-testing:2.5.3")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")

    testImplementation("com.google.truth:truth:1.1.2")

    testImplementation("com.google.dagger:hilt-android-testing:2.45")
    kaptTest("com.google.dagger:hilt-android-compiler:2.45")

    testImplementation("org.robolectric:robolectric:4.10")

    testImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}