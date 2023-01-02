// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    val kotlinVersion = "1.7.20"
    val kspVersion = "1.0.7"
    id("com.android.application") version "7.3.1" apply false
    id("com.android.library") version "7.3.1" apply false
    id("org.jetbrains.kotlin.android") version kotlinVersion apply false
    id("com.google.devtools.ksp") version "$kotlinVersion-$kspVersion" apply false
    id("com.flaviofaria.catalog") version "0.1.0" apply false
}
