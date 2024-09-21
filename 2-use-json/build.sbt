ThisBuild / scalaVersion := "3.5.1"

libraryDependencies ++= Seq(
  "dev.vhonta" %% "zio-temporal-core" % "0.6.1",
  "org.slf4j" % "slf4j-nop" % "2.0.16",
)

scalacOptions ++= Seq("-deprecation")
