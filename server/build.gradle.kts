plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("application")
    id("distribution")
}

val kotlinVersion = project.property("kotlin.version") as String
val kotlinxSerializationVersion = project.property("kotlinx.serialization.version") as String
val ktorVersion = project.property("ktor.version") as String
val kotlinWrappersSuffix = project.property("kotlin.wrappers.suffix") as String
val logbackVersion = project.property("logback.version") as String
val ktorJsoupVersion = "1.6.4"

fun kotlinWrappers(target: String): String =
    "org.jetbrains.kotlin-wrappers:kotlin-$target"

dependencies {
    implementation(project(":shared"))

    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")

    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-html-builder:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    runtimeOnly("ch.qos.logback:logback-classic:$logbackVersion")

    implementation(enforcedPlatform(kotlinWrappers("wrappers-bom:0.0.1-${kotlinWrappersSuffix}")))
    implementation(kotlinWrappers("css"))

    implementation ("com.tfowl.ktor:ktor-jsoup:$ktorJsoupVersion")

    implementation ("io.ktor:ktor-client-cio:$ktorVersion")


    testImplementation(kotlin("test"))
}

application {
    mainClass.set("MainKt")
}

tasks.test {
    useJUnitPlatform()
}