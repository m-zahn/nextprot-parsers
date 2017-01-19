//You can check configuration examples here:
//http://www.scala-sbt.org/release/docs/Examples/Quick-Configuration-Examples

import AssemblyKeys._ // put this at the top of the file

assemblySettings

jarName in assembly := "nextprot-scala-parsers.jar"

mainClass in assembly := Some("org.nextprot.parser.core.NXParserApp")

name := "nextprot-scala-parsers"

organization := "org.nextprot.parser"

version := "1.1.0-SNAPSHOT"

description := "Nextprot Scala Parsers"

scalaVersion := "2.11.8"

scalacOptions ++= Seq( "-deprecation", "-unchecked", "-feature" )

fork := true

// 
// all of them => "-Dfiles.expression=^ENSG.*.xml$",
// 225 samples => "-Dfiles.expression=^ENSG0000000.*.xml$",
//  10 samples => "-Dfiles.expression=ENSG00000100417.xml|ENSG00000113391.xml|ENSG00000116127.xml|ENSG00000124782.xml|ENSG00000156463.xml|ENSG00000160799.xml|ENSG00000166908.xml|ENSG00000168876.xml|ENSG00000172660.xml|ENSG00000176946.xml",


// old core module options to be deleted if not useful
//javaOptions ++= Seq("-Dparser.impl=org.nextprot.parser.core.ParserTest",
//"-Dfiles.directory=/tmp/hpa-data",
//"-Dfiles.expression=^ENSG.*.xml$"
//)

// hpa module options
javaOptions ++= Seq(
"-Xmx2000m",
//"-Dfiles.directory=/tmp/ENS",
"-Dfiles.directory=hpa-data",
"-Dfiles.expression=^ENSG.*.xml$",
"-Doutput.file=/tmp/hpa/output.xml",
"-Dfailed.file=/tmp/hpa/failed.xml",
"-Dhpa.mapping.file=src/test/resources/HPA_Subcell_Mapping.txt",
"-Dhpa.tissue.mapping.file=src/test/resources/NextProt_tissues.from-db.txt",
"-Dhpa.anti.multi.file=src/test/resources/multi_target_antibodies.txt"
//"-Dfiles.entries=src/test/resources/org/nextprot/parser/ensg/entry-list.txt"
//"-Doutput.file=sp_ensg.txt"
)

resolvers += "nexus" at "http://miniwatt:8800/nexus/content/groups/public/"


libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.11" % "3.0.1" % "test",
  "com.typesafe.akka" %% "akka-actor" % "2.4.16",
  "com.typesafe.akka" %% "akka-testkit" % "2.4.16",
  "junit" % "junit" % "4.11" % "test",
  "org.apache.jena" % "apache-jena-libs" % "3.0.1",
  "com.novocode" % "junit-interface" % "0.7" % "test->default",
  "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.5",
  "code.google.com" % "xml-test" % "0.3.0" % "test",
  "org.nextprot" % "nextprot-commons" % "2.1.0-SNAPSHOT"
)

// Publish section ////////////////////////////////////////////////////////////////////////////////////////////////////////////
publishTo := {
  val nexus = "http://miniwatt.isb-sib.ch:8800/"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "nexus/content/repositories/nextprot-snapshot-repo")
  else
    Some("releases"  at nexus + "nexus/content/repositories/nextprot-repo")
}

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra :=
<licenses>
  <license>
    <name>Apache 2</name>
    <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
    <distribution>repo</distribution>
  </license>
</licenses>

crossPaths := false