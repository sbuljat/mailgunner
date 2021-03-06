organization  := "com.sbuljat"

name := "mailgunner"

version       := "0.1"

scalaVersion  := "2.11.7"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-Ywarn-dead-code", "-feature", "-language:implicitConversions")

libraryDependencies ++= {
  val akkaVersion = "2.4.1"
  val sprayVersion = "1.3.3"

  Seq(
    "io.spray"                   %%  "spray-can"         % sprayVersion,
    "io.spray"                   %%  "spray-routing"     % sprayVersion,
    "io.spray"                   %%  "spray-client"      % sprayVersion,
    "io.spray"                   %%  "spray-json"        % "1.3.2",
    "io.spray"                   %%  "spray-testkit"     % sprayVersion  % "test",
    "com.typesafe.akka"          %%  "akka-actor"        % akkaVersion,
    "com.typesafe.akka"          %%  "akka-persistence"  % akkaVersion,
    "org.iq80.leveldb"           %   "leveldb"           % "0.7",
    "org.fusesource.leveldbjni"  %   "leveldbjni-all"    % "1.8",
    "com.typesafe.akka"          %%  "akka-testkit"      % akkaVersion   % "test",
    "org.specs2"                 %%  "specs2-core"       % "2.4.17" % "test",
    "joda-time"                  %   "joda-time"         % "2.7",
    "commons-validator"          %   "commons-validator" % "1.4.1"
  )
}

Revolver.settings