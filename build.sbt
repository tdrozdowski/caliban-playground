name := "caliban-playground"

version := "0.1"

scalaVersion := "2.13.4"

idePackagePrefix := Some("dev.xymox.caliban.playground")

val calibanVersion = "0.9.4"
val zioVersion     = "1.0.3"

libraryDependencies ++= Seq(
  "com.github.ghostdogpr" %% "caliban"           % calibanVersion,
  "com.github.ghostdogpr" %% "caliban-http4s"    % calibanVersion,
  "dev.zio"               %% "zio-test"          % zioVersion % Test,
  "dev.zio"               %% "zio-test-sbt"      % zioVersion % Test,
  "dev.zio"               %% "zio-test-magnolia" % zioVersion % Test
)
