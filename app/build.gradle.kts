plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
    id("androidx.room")
    id("kotlin-parcelize")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.24"
}

android {
    namespace = "com.example.musiccontactapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.musiccontactapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    room {

        schemaDirectory(layout.projectDirectory.dir("schemas"))
    }
}

dependencies {


        implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    val room_version = "2.7.2"
        implementation("androidx.room:room-runtime:$room_version")
        ksp("androidx.room:room-compiler:$room_version")
        implementation("androidx.room:room-ktx:$room_version")


        implementation(platform("androidx.compose:compose-bom:2025.04.00"))
        implementation("androidx.compose.material3:material3:1.3.0")
        implementation("androidx.compose.ui:ui")
        implementation("androidx.compose.ui:ui-graphics")
        implementation("androidx.compose.ui:ui-tooling-preview")
        debugImplementation("androidx.compose.ui:ui-tooling")


        implementation("androidx.navigation:navigation-compose:2.8.5")


        implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")



        implementation("com.google.dagger:hilt-android:2.55")
        ksp("com.google.dagger:hilt-compiler:2.55")
        implementation("androidx.hilt:hilt-navigation-compose:1.2.0")


        implementation("com.squareup.retrofit2:retrofit:2.11.0")
        implementation("com.squareup.retrofit2:converter-gson:2.11.0")
        implementation("com.squareup.okhttp3:okhttp:4.12.0")


        implementation("io.coil-kt:coil-compose:2.7.0")


        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")


        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.activity.compose)

        coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")

        implementation("androidx.navigation:navigation-compose:2.7.7")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    implementation (libs.androidx.core.ktx)

    implementation ("com.airbnb.android:lottie-compose:6.0.0")

    implementation ("com.google.accompanist:accompanist-permissions:0.33.2-alpha")

    testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
        androidTestImplementation(platform(libs.androidx.compose.bom))
        androidTestImplementation(libs.androidx.ui.test.junit4)
        debugImplementation(libs.androidx.ui.test.manifest)

}