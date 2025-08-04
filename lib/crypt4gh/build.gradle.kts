plugins {
    id("java")
    id("extra-java-module-info")
    id("io.freefair.lombok") version "8.14"
    id("formatting-conventions")
    id("maven-publish")
}
group = "no.elixir"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.slf4j:slf4j-api:2.0.17")
    implementation("org.slf4j:slf4j-simple:2.0.17")
    implementation("org.apache.commons:commons-lang3:3.18.0")
    implementation("commons-codec:commons-codec:1.19.0")
    implementation("commons-cli:commons-cli:1.10.0")
    implementation("commons-io:commons-io:2.20.0")
    implementation("com.rfksystems:blake2b:2.0.0")
    implementation("at.favre.lib:bkdf:0.6.0")
    implementation("com.lambdaworks:scrypt:1.4.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.13.4")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

extraJavaModuleInfo {
    automaticModule("bcrypt-0.10.2.jar", "bcrypt")
    automaticModule("bkdf-0.6.0.jar", "bkdf")
    automaticModule("scrypt-1.4.0.jar", "scrypt")
}

tasks.test {
    useJUnitPlatform()
}
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "fega-norway-crypt4gh"
            url = uri("https://maven.pkg.github.com/Parisa68/FEGA-Norway")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
