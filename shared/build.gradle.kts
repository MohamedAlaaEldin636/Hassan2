import com.mohamedalaa.moviestvshows.dependencies.*

plugins {
    id("com.android.library")

    kotlin("android")

    kotlin("kapt")

    id("dagger.hilt.android.plugin")

    id("androidx.navigation.safeargs.kotlin")

    id("com.google.gms.google-services")
}

android {
    compileSdk = Versions.compile_sdk
    buildToolsVersion = Versions.build_tools

    defaultConfig {
        minSdk = Versions.min_sdk
        targetSdk = Versions.target_sdk

        // Used to use VectorDrawableCompat in XML isa.
        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner = Const.test_instrumentation_runner

        // Needed when number of methods exceeds specific amount.
        multiDexEnabled = true
    }

    buildTypes {
        debug {
            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = false
            isShrinkResources = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    compileOptions {
        // Flag to enable support for the new language APIs
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = Versions.jvm_11
        freeCompilerArgs = freeCompilerArgs + listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    //api("com.github.chintan369:MultiImagePicker:1.0.8")
    //api("gun0912.ted:tedbottompicker:2.0.1")
    //api("com.asksira.android:bsimagepicker:1.3.2")
    //api("io.github.ParkSangGwon:tedimagepicker:1.2.8")

    api("com.facebook.android:facebook-login:13.2.0")

    api("com.github.bumptech.glide:okhttp3-integration:4.13.0")

    api(Deps.pusher)
    api("com.google.firebase:firebase-core:21.0.0")
    api("com.google.firebase:firebase-messaging:23.0.5")
    api("com.pusher:push-notifications-android:1.9.0")
    api("com.google.firebase:firebase-iid:21.1.0")
    api("com.google.firebase:firebase-analytics-ktx:21.0.0")
    api("com.google.firebase:firebase-database-ktx:20.0.5")

    api("com.google.maps.android:android-maps-utils:2.3.0")

    coreLibraryDesugaring(Deps.jdk_desugar)

    implementation(Deps.dagger.hilt_android)
    kapt(Deps.dagger.hilt_android_compiler)

    api(Deps.kotlin.stdlib_jdk8)

    api(Deps.multidex)

    api(Deps.androidx.core.core_ktx)
    api(Deps.androidx.app_compat.app_compat)
    api(Deps.material)
    api(Deps.androidx.constraint_layout.constraint_layout)

    api(Deps.androidx.navigation.fragment_ktx)
    api(Deps.androidx.navigation.ui_ktx)
    api(Deps.androidx.hilt.navigation_fragment)

    api(Deps.androidx.paging.runtime)

    api(Deps.timber)

    api(Deps.toasty)

    api(Deps.androidx.lifecycle.view_model_ktx)
    api(Deps.androidx.lifecycle.saved_state)
    api(Deps.androidx.lifecycle.live_data_ktx)
    api(Deps.androidx.lifecycle.lifecycle_runtime_ktx)
    api(Deps.androidx.lifecycle.common_java8)

    api(Deps.androidx.datastore.preferences)

    api(Deps.gson)

    api(Deps.lottie)
    api(Deps.slider_view)

    api(Deps.squareup.okhttp3.logging_interceptor)
    api(Deps.squareup.retrofit2.retrofit)
    api(Deps.squareup.retrofit2.converter_gson)

    api(Deps.google_map)
    api(Deps.google_location)
    api(Deps.google_places)

    api(Deps.localization)

    api(Deps.exo_player)

    implementation(Deps.glide.glide)
    kapt(Deps.glide.compiler)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}

// Used due to this error when building the project
// error -> https://github.com/google/dagger/issues/2123
// Another work around would be instead of using dependencies of local/remote in the repo classes
// constructor you would have used them as @Inject property not in constructor, but this would
// require more code, which is unnecessary with this solution isa.
hilt {
    enableAggregatingTask = true
}
