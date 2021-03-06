
lazy val root = (project in file("."))
  .settings(name := "user-management-lagom")
  .aggregate(userApi, userImpl,
    jsonformats, utils)
  .settings(commonSettings: _*)

organization in ThisBuild := "ca.example"

scalaVersion in ThisBuild := "2.12.6"

version in ThisBuild := "1.0.0-SNAPSHOT"

val playJsonDerivedCodecs = "org.julienrf" %% "play-json-derived-codecs" % "4.0.0"
val macwire = "com.softwaremill.macwire" %% "macros" % "2.2.5" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1" % "test"
val jbcrypt = "org.mindrot" % "jbcrypt" % "0.3m"

lazy val jsonformats = (project in file("json-formats"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      playJsonDerivedCodecs
    )
  )

lazy val utils = (project in file("utils"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi,
      lagomScaladslServer,
      scalaTest
    )
  )
  .dependsOn(jsonformats)

lazy val userApi = (project in file("user-api"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi,
      playJsonDerivedCodecs
    )
  )
  .dependsOn(jsonformats, utils)

lazy val userImpl = (project in file("user-impl"))
  .settings(commonSettings: _*)
  .enablePlugins(LagomScala)
  .dependsOn(userApi)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      macwire,
      jbcrypt,
      scalaTest
    )
  ).dependsOn(emailApi)

lazy val emailApi = (project in file("email-api"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi,
      playJsonDerivedCodecs
    )
  )
  .dependsOn(jsonformats, utils)

lazy val emailImpl = (project in file("email-impl"))
  .settings(commonSettings: _*)
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      macwire,
      scalaTest
    )
  ).dependsOn(userApi, emailApi)

def commonSettings: Seq[Setting[_]] = Seq(
)

lagomCassandraCleanOnStart in ThisBuild := false
