plugins {
    id("java")
    application
}

group = "org.yates"
version = "1.0"

repositories {
    mavenCentral()
}

application {
    mainClass.set("org.yates.Main")
}