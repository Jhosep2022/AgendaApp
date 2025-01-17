plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.agenda_online"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.agenda_online"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.lottie)
    implementation(platform("com.google.firebase:firebase-bom:33.1.2")) // la plataforma
    implementation("com.google.firebase:firebase-database:21.0.0") // base de datos
    implementation("com.google.firebase:firebase-auth:21.0.1") // logearse
    implementation("com.google.firebase:firebase-analytics-ktx") // analisis de los usuario
    implementation("com.google.firebase:firebase-storage:20.0.1") // Añadir esta línea para Firebase Storage
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0") // Añadir esta línea para Glide

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

