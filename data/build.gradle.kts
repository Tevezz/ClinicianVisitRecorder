plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.matheus.clinicianvisitrecorder.data"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 30
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

}

kotlin {
    jvmToolchain(11)
}

dependencies {
    implementation(project(":domain"))
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.paging.runtime)
}