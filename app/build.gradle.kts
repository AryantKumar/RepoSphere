plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.kapt") // Add this line

}

android {
    namespace = "com.aryant.github"
    compileSdk = 35
    android {
        defaultConfig {
            manifestPlaceholders["appAuthRedirectScheme"] = "com.aryant.github"
        }
    }


    defaultConfig {
        applicationId = "com.aryant.github"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    android {
        signingConfigs {
            create("release") {
                storeFile = file("C:\\Users\\ARYANT\\Desktop\\github.jks") // Path to your keystore file
                storePassword = "Aryant@123"
                keyAlias = "key0"
                keyPassword = "Aryant@321"
            }
        }

        buildTypes {
            release {
                isMinifyEnabled = true
                signingConfig = signingConfigs.getByName("release")
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }

            buildFeatures {
                viewBinding = true
            }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }
            kotlinOptions {
                jvmTarget = "11"
            }
        }

        dependencies {

            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.appcompat)
            implementation(libs.material)
            implementation(libs.androidx.activity)
            implementation(libs.androidx.constraintlayout)
            testImplementation(libs.junit)
            androidTestImplementation(libs.androidx.junit)
            androidTestImplementation(libs.androidx.espresso.core)
            // Lifecycle (ViewModel, LiveData, Runtime)
            implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
            implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
            implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

            // Google Sign-In & Firebase Authentication
            implementation("com.google.android.gms:play-services-auth:20.7.0")
            implementation("com.google.firebase:firebase-auth-ktx:22.2.0")
            implementation(platform("com.google.firebase:firebase-bom:33.9.0"))
            implementation("com.google.firebase:firebase-analytics")

            // Retrofit (Networking)
            implementation("com.squareup.retrofit2:retrofit:2.10.0")
            implementation("com.squareup.retrofit2:converter-gson:2.10.0")
            implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.11")
            implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.11")

            // Room Database (Offline Caching)
            implementation("androidx.room:room-runtime:2.6.1")
            kapt("androidx.room:room-compiler:2.6.1")
            implementation("androidx.room:room-ktx:2.6.1")

            // Hilt for Dependency Injection
            implementation("com.google.dagger:hilt-android:2.51")
            implementation("com.google.dagger:hilt-android:2.51")
            kapt("com.google.dagger:hilt-android-compiler:2.51")
            // Firebase Cloud Messaging (Push Notifications)
            implementation("com.google.firebase:firebase-messaging-ktx:23.4.0")

            // Kotlin Coroutines
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")

//       Navigation Component
            implementation("androidx.navigation:navigation-fragment-ktx:2.8.0")
            implementation("androidx.navigation:navigation-ui-ktx:2.8.0")

            // Glide (Image Loading)
            implementation("com.github.bumptech.glide:glide:4.16.0")
            kapt("com.github.bumptech.glide:compiler:4.16.0")
            dependencies {
                implementation("com.google.android.gms:play-services-auth:20.7.0") // Google Sign-In
                implementation("com.google.firebase:firebase-auth-ktx:22.1.2") // Firebase Auth
            }


        }
    }
    dependencies {
        implementation(libs.firebase.auth)
        implementation(libs.androidx.credentials)
        implementation(libs.androidx.credentials.play.services.auth)
        implementation(libs.googleid)
    }
}
dependencies {
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
}
dependencies {
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
}

dependencies {
    implementation("net.openid:appauth:0.11.1") // OAuth library
}
dependencies {
    implementation("androidx.recyclerview:recyclerview:1.3.2")
}
dependencies {
    implementation ("androidx.security:security-crypto:1.1.0-alpha06")
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
}

