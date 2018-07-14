name := """wedding-scala"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers += Resolver.sonatypeRepo("snapshots")

scalaVersion := "2.12.6"

crossScalaVersions := Seq("2.11.12", "2.12.4")

libraryDependencies += guice
libraryDependencies += jdbc
libraryDependencies += evolutions
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += "com.vladsch.flexmark" % "flexmark-all" % "0.32.24"
libraryDependencies += "io.circe" %% "circe-yaml" % "0.6.1"
libraryDependencies += "org.playframework.anorm" %% "anorm" % "2.6.1"
libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.23.1"
libraryDependencies ++= Seq(
  "ch.qos.logback"  %  "logback-classic"    % "1.2.3",
  "com.typesafe.play" %% "anorm" % "2.5.1"
)

