plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.applogin"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.applogin"
        minSdk = 32
        targetSdk = 36
        versionCode = 4
        versionName = "v4"
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

    packaging {
        resources {
            excludes += "/META-INF/AL2.0"
            excludes += "/META-INF/LGPL2.1"
            excludes += "/META-INF/NOTICE*"
            excludes += "/META-INF/LICENSE*"
            excludes += "/META-INF/DEPENDENCIES"
            excludes += "/META-INF/mime.types"
        }
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.activity:activity:1.8.0")
    implementation("org.apache.commons:commons-email2-jakarta:2.0.0-M1")
}

// Evita conflitos caso uma biblioteca antiga traga os artefatos JDK 7/8 do Kotlin.
configurations.all {
    exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk7")
    exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk8")
}
