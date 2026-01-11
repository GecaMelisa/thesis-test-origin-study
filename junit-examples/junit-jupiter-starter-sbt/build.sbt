import com.github.sbt.jacoco.JacocoPlugin
import com.github.sbt.jacoco.JacocoPlugin.autoImport._

ThisBuild / organization := "com.example"
ThisBuild / scalaVersion := "3.7.3"
ThisBuild / version := "0.1.0-SNAPSHOT"

lazy val root = project
  .in(file("."))
  .enablePlugins(JacocoPlugin)
  .settings(
    name := "junit-jupiter-starter-sbt",
    libraryDependencies ++= Seq(
      "net.aichler" % "jupiter-interface" % JupiterKeys.jupiterVersion.value % Test,
      "org.junit.jupiter" % "junit-jupiter" % "5.13.4" % Test,
      "org.junit.platform" % "junit-platform-launcher" % "1.13.4" % Test,
    ),
    testOptions += Tests.Argument(jupiterTestFramework, "--display-mode=tree"),
    Test / fork := true,
    Test / javaOptions ++= sys.props.get("includeTags").filter(_.nonEmpty).toSeq
      .map(t => s"-Djunit.jupiter.tags.include=$t"),
    Test / javaOptions ++= sys.props.get("excludeTags").filter(_.nonEmpty).toSeq
      .map(t => s"-Djunit.jupiter.tags.exclude=$t"),
    Test / testOptions ++= sys.props.get("includeTags").filter(_.nonEmpty).toSeq.map { tag =>
      val tagToPrefix = Map(
        "human" -> "com.example.project.HumanCalculatorTests",
        "gpt" -> "com.example.project.CalculatorTests",
        "codex" -> "com.example.project.CalculatorTestCodex"
      )
      val allowedPrefix = tagToPrefix.getOrElse(tag, "")
      Tests.Filter(name => allowedPrefix.nonEmpty && name.startsWith(allowedPrefix))
    }
  )
