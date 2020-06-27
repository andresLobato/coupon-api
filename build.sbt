
name := """coupon-api"""

organization := "com.coupon.api"

version := "1.0"

scalaVersion := "2.13.2"

lazy val root = (project in file("."))
  .enablePlugins(PlayJava)
  .disablePlugins(PlayFilters)
  .settings(
    libraryDependencies ++= Seq(
      javaWs ,guice, ehcache,
      "com.github.dpaukov" % "combinatoricslib3" % "3.3.0",
      "org.apache.commons" % "commons-lang3" % "3.10",
      "org.mockito" % "mockito-core" % "2.10.0" % "test"
    )
  )

parallelExecution in Test := false

javaOptions in Test ++= Seq(
  "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=9998",
  "-Xms512M",
  "-Xmx1536M",
  "-Xss1M",
  "-XX:MaxPermSize=384M"
)

// JaCoCo Coverage Report Settings
jacocoReportSettings := JacocoReportSettings(
  "Jacoco Coverage Report",
  None,
  JacocoThresholds(
    instruction = 0,
    method = 0,
    branch = 0,
    complexity = 0,
    line = 0,
    clazz = 0
  ),
  Seq(JacocoReportFormats.HTML),
  "utf-8"
)

jacocoExcludes in Test := Seq(
  "controllers.Reverse*",
  "controllers.javascript.*",
  "jooq.*",
  "Module",
  "router.Routes*",
  "*.routes*"
)