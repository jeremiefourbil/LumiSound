name := "LumiSound"

scalaVersion := "2.10.1"

libraryDependencies ++= Seq("org.specs2" %% "specs2" % "1.14" % "test",
			    "org.scalacheck" %% "scalacheck" % "1.10.1" % "test",
			    "com.typesafe.akka" %% "akka-actor" % "2.1.4",
			    "com.typesafe.akka" %% "akka-remote" % "2.1.4",
			    "com.typesafe.akka" %% "akka-kernel" % "2.1.4",
			    "org.jfree" % "jfreechart" % "1.0.14",
			    "io.spray" % "spray-can" % "1.1-M8",
  				"io.spray" % "spray-routing" % "1.1-M8",
  				"io.spray" % "spray-testkit" % "1.1-M8")

resolvers ++= Seq("Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
"spray repo" at "http://repo.spray.io/")
