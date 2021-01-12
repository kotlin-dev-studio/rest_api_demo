// stuff from src/test is published into a special JAR on which test code in other modules can depend on

tasks.register<testJar>("Jar")

task testJar(type: Jar) {
    classifier = 'test'
    from sourceSets.test.output
}

artifacts {
    testRuntimeOnly(testJar)
}

//dependencies {
//    compile "org.springframework.boot:spring-boot-starter-data-jpa"
//    compile "com.h2database:h2:${h2Version}"
//}
