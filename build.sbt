import play.Project._
import com.github.play2war.plugin._

name := "antarcticle-scala"

version := "0.1"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "1.0.1",
  // Joda time wrapper for scala
  "com.github.nscala-time" %% "nscala-time" % "0.6.0",
  "com.h2database" % "h2" % "1.3.174",
  "org.mockito" % "mockito-all" % "1.9.0",
  // markdown support
  "org.tautua.markdownpapers" % "markdownpapers-core" % "1.4.2",
  "org.scalaz" %% "scalaz-core" % "7.0.5",
  "org.typelevel" %% "scalaz-specs2" % "0.1.5" % "test"
)

playScalaSettings

// Coffee Script compilation options
coffeescriptOptions := Seq("bare")

// WAR packaging

Play2WarPlugin.play2WarSettings

Play2WarKeys.servletVersion := "3.0"

// disable publishing the main API jar
publishArtifact in (Compile, packageDoc) := false

// disable publishing the main sources jar
publishArtifact in (Compile, packageSrc) := false