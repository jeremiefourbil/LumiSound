name := "LumiSound"

scalaVersion := "2.10.1"

libraryDependencies ++= Seq("org.specs2" %% "specs2" % "1.14" % "test",
			    "org.scalacheck" %% "scalacheck" % "1.10.1" % "test",
			    "com.typesafe.akka" %% "akka-actor" % "2.1.4",
			    "com.typesafe.akka" %% "akka-remote" % "2.1.4",
			    "com.typesafe.akka" %% "akka-kernel" % "2.1.4",
			    "org.jfree" % "jfreechart" % "1.0.14")

resolvers ++= Seq("Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/")
