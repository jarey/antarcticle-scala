import com.github.play2war.plugin._
import play.Project._

name := "antarcticle-scala"

version := "2.6"

scalaVersion := "2.10.5-RC1"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "2.0.2",
  // Joda time wrapper for scala
  "com.github.nscala-time" %% "nscala-time" % "0.6.0",
  // 1.3.174 has problem fixed in trunk:
  // https://code.google.com/p/h2database/source/detail?r=5363
  "com.h2database" % "h2" % "1.3.173",
  "org.mockito" % "mockito-all" % "1.9.5" % "test",
  // markdown support
  "org.pegdown" % "pegdown" % "1.4.2",
  // scalaz magic
  "org.scalaz" %% "scalaz-core" % "7.0.5",
  "org.typelevel" %% "scalaz-specs2" % "0.1.5" % "test",
  // production database
  "mysql" % "mysql-connector-java" % "5.1.28"
)

playScalaSettings

// global imports for templates
templatesImport ++= Seq(
  "security.Entities._",
  "security.Permissions._",
  "security.Authorities._",
  "security.Principal"
)

// publish some SBT variables as scala object for application code
buildInfoSettings

sourceGenerators in Compile <+= buildInfo

buildInfoKeys := Seq[BuildInfoKey](version)

buildInfoKeys ++= Seq[BuildInfoKey](
  BuildInfoKey.action("fullVersion") {
    sys.props.getOrElse("app.version", (version in version.scope).value + "-dev")
  }
)

buildInfoPackage := "build"

// Coffee Script compilation options
coffeescriptOptions := Seq("bare")

scalacOptions ++= Seq("-feature")

// WAR packaging

Play2WarPlugin.play2WarSettings

Play2WarKeys.servletVersion := "3.0"

// disable publishing the main API jar
publishArtifact in (Compile, packageDoc) := false

// disable publishing the main sources jar
publishArtifact in (Compile, packageSrc) := false

instrumentSettings

ScoverageKeys.excludedPackages in ScoverageCompile := "<empty>"

ScoverageKeys.highlighting := true
