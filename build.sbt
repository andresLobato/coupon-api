name := """coupon-api"""
organization := "com.coupon.api"

version := "1.0-SNAPSHOT"

scalaVersion := "2.13.2"

lazy val root = (project in file("."))
  .enablePlugins(PlayJava)
  .settings(
    libraryDependencies ++= Seq(
      javaWs ,guice, ehcache,
      "com.github.dpaukov" % "combinatoricslib3" % "3.3.0",
      "org.apache.commons" % "commons-lang3" % "3.10"
    )
  )



