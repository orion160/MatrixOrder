plugins {
    java
    application
    id("com.diffplug.spotless") version "6.25.0"
}

group "edu.uniandes"
version "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
        vendor.set(JvmVendorSpec.ADOPTIUM)
    }
}

application {
    mainClass.set("edu.uniandes.App")
    mainModule.set("edu.uniandes.MatrixOrder")
}

tasks.compileJava {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-Xlint:unchecked")
    options.compilerArgs.add("-Xlint:deprecation")
}

spotless {
    encoding("UTF-8")
    java {
        googleJavaFormat().reflowLongStrings()
        formatAnnotations()
    }
    kotlinGradle {

    }
}

dependencies {
}
