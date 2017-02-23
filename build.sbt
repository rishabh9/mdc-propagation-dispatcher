import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

organization := "com.github.rishabh9"

name := "mdc-propagation-dispatcher"

version := (version in ThisBuild).value

scalaVersion := "2.11.7"

exportJars := true

retrieveManaged := true

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.3.0" % "provided",
  "com.typesafe.play" %% "play" % "2.5.10" % "provided",
  "org.slf4j" % "slf4j-api" % "1.7.21" % "provided",
  "com.typesafe.akka" %% "akka-actor" % "2.4.12" % "provided"
)

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

initialize := {
  val _ = initialize.value
  if (sys.props("java.specification.version") != "1.8")
    sys.error("Java 8 is required for this project.")
}

sources in (Compile, doc) := Seq.empty

// RELEASE --

// Maven publishing info
publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

releasePublishArtifactsAction := PgpKeys.publishSigned.value

// The Release configuration
releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,              // : ReleaseStep
  inquireVersions,                        // : ReleaseStep
  runTest,                                // : ReleaseStep
  setReleaseVersion,                      // : ReleaseStep
  commitReleaseVersion,                   // : ReleaseStep, performs the initial git checks
  tagRelease,                             // : ReleaseStep
  publishArtifacts, // : ReleaseStep, checks whether `publishTo` is properly set up
  setNextVersion,                         // : ReleaseStep
  commitNextVersion,                      // : ReleaseStep
  pushChanges                             // : ReleaseStep, also checks that an upstream branch is properly configured
)

pomExtra := (
  <url>https://github.com/rishabh9/mdc-propagation-dispatcher/</url>

    <licenses>
      <license>
        <name>Apache 2</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      </license>
    </licenses>

    <scm>
      <connection>scm:git:git@github.com:rishabh9/mdc-propagation-dispatcher.git</connection>
      <developerConnection>scm:git:git@github.com:rishabh9/mdc-propagation-dispatcher.git</developerConnection>
      <url>https://github.com/rishabh9/mdc-propagation-dispatcher/</url>
    </scm>

    <developers>
      <developer>
        <id>rishabh9</id>
        <name>Rishabh Joshi</name>
        <url>https://github.com/rishabh9</url>
      </developer>
    </developers>
  )