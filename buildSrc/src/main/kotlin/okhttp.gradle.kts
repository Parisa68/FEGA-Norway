plugins {
    java
}

repositories { mavenCentral() }

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.12.1")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.1")
}
