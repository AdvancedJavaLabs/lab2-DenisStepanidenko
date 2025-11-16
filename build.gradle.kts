plugins {
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.spring") version "1.9.20"
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
    application
}

group = "org.itmo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("javax.jms:javax.jms-api:2.0.1")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.kafka:spring-kafka")
    implementation ("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation ("com.fasterxml.jackson.core:jackson-core:2.15.2")
    implementation ("com.fasterxml.jackson.core:jackson-annotations:2.15.2")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation(kotlin("test"))

    // Stanford CoreNLP для анализа тональности
    implementation("edu.stanford.nlp:stanford-corenlp:4.5.0")
    implementation("edu.stanford.nlp:stanford-corenlp:4.5.0:models")
    implementation("edu.stanford.nlp:stanford-corenlp:4.5.0:models-english")

    // OpenNLP для NER (распознавания имен)
    implementation("org.apache.opennlp:opennlp-tools:2.2.0")



    // Дополнительные зависимости для Stanford NLP
    implementation("com.google.protobuf:protobuf-java:3.21.12")
    implementation("org.slf4j:slf4j-api:2.0.7")
}


tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}

tasks.withType<Test> {
    systemProperty("file.encoding", "UTF-8")
}

tasks.withType<JavaExec> {
    systemProperty("file.encoding", "UTF-8")
}


tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
    systemProperty("file.encoding", "UTF-8")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("MainKt")
}