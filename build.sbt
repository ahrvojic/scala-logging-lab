ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.12.20"

lazy val log4j2 = project
  .settings(
    libraryDependencies ++= Seq(
      "com.fasterxml.jackson.core" % "jackson-databind" % "2.15.2",
      "org.apache.logging.log4j" %% "log4j-api-scala" % "13.1.0",
      "org.apache.logging.log4j" % "log4j-core" % "2.25.1",
      "org.apache.logging.log4j" % "log4j-layout-template-json" % "2.25.1",
    )
  )

lazy val log4j2_async = project
  .settings(
    libraryDependencies ++= Seq(
      "com.fasterxml.jackson.core" % "jackson-databind" % "2.15.2",
      "com.lmax" % "disruptor" % "4.0.0",
      "org.apache.logging.log4j" %% "log4j-api-scala" % "13.1.0",
      "org.apache.logging.log4j" % "log4j-core" % "2.25.1",
      "org.apache.logging.log4j" % "log4j-layout-template-json" % "2.25.1",
    )
  )

lazy val tinylog_jackson = project
  .settings(
    libraryDependencies ++= Seq(
      "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % "2.15.2",
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.15.2",
      "org.tinylog" % "tinylog-api" % "2.7.0",
      "org.tinylog" % "tinylog-impl" % "2.7.0",
    )
  )

lazy val tinylog_jsoniter = project
  .settings(
    libraryDependencies ++= Seq(
      "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-core" % "2.37.6",
      "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-macros" % "2.37.6" % "compile-internal",
      "org.tinylog" % "tinylog-api" % "2.7.0",
      "org.tinylog" % "tinylog-impl" % "2.7.0",
    )
  )

