// Top-level build file where you can add configuration options common to all sub-projects/modules.
import java.util.Properties
plugins {
    alias(libs.plugins.android.application) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}
