
name := """coupon-api"""

organization := "com.coupon.api"

version := "1.0"

scalaVersion := "2.13.2"

lazy val root = (project in file("."))
  .enablePlugins(PlayJava)
  .settings(
    libraryDependencies ++= Seq(
      javaWs ,guice, ehcache,
      "com.github.dpaukov" % "combinatoricslib3" % "3.3.0",
      "org.apache.commons" % "commons-lang3" % "3.10",
      "org.mockito" % "mockito-core" % "2.10.0" % "test"
    )
  )

javaOptions in Test ++= Seq(
  "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=9998",
  "-Xms512M",
  "-Xmx1536M",
  "-Xss1M",
  "-XX:MaxPermSize=384M"
)