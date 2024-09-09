ThisBuild / scalaVersion := "3.5.0"

libraryDependencies ++= Seq(
  "dev.vhonta" %% "zio-temporal-core" % "0.6.1",
//  "dev.zio" %% "zio-logging" % "2.3.1",
    "org.slf4j" % "slf4j-simple" % "2.0.16",
//  "org.slf4j" % "slf4j-nop" % "2.0.16",
)

scalacOptions ++= Seq("-deprecation")
