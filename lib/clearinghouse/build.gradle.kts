plugins {
    id("java")
    id("maven-publish")
    id("io.freefair.lombok") version "9.1.0"
    id("formatting-conventions")
    id("jsonwebtoken")
    id("okhttp")
}
group = "no.elixir"

repositories {
    mavenCentral()
}
java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    implementation("org.apache.commons:commons-collections4:4.5.0")
    implementation("org.apache.commons:commons-lang3:3.20.0")
    implementation("com.google.code.gson:gson:2.13.2")
    implementation("com.auth0:jwks-rsa:0.23.0")
    implementation("com.github.ben-manes.caffeine:caffeine:3.2.3")
    implementation("org.slf4j:slf4j-jdk14:2.0.17")
    testImplementation("org.junit.jupiter:junit-jupiter-api:6.0.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.bouncycastle:bcprov-jdk15to18:1.83")
    testImplementation("org.bouncycastle:bcpkix-jdk15to18:1.83")
}
// cmt test
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "fega-norway-clearinghouse"
            url = uri("https://maven.pkg.github.com/Parisa68/FEGA-Norway")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
