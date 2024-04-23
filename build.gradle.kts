plugins {
    kotlin("jvm") version "1.9.23"
}

group = "me.meiallu.luck"
version = "1.0"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {

}

kotlin {
    jvmToolchain(17)
}