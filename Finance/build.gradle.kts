// Top-level build file where you can add configuration options common to all sub-projects/modules.
//plugins {
//    id("com.android.application") version "8.9.1" apply false //
//    alias(libs.plugins.kotlin.android) version "1.9.22"
//    alias(libs.plugins.kotlin.compose) apply false
//}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.9.1" apply false
    id("com.android.library") version "8.9.1" apply false

    // 🛠️ Subimos Kotlin a la versión 2.1.0 que piden tus librerías
    id("org.jetbrains.kotlin.android") version "2.1.0" apply false

    // 🛠️ Ponemos el KSP exacto y compatible para Kotlin 2.1.0
    id("com.google.devtools.ksp") version "2.1.0-1.0.29" apply false
}