package services.impl

import java.io.File

import models.{Location, Step, Trip}
import services.TripRepository

import scala.io.Source

class TripFileSystemRepository extends TripRepository {

  override def get = {
    def getDirectory(path: String): Option[File] = {
      val dir = new File(getClass.getClassLoader.getResource(path).toURI)
      if (dir.exists && dir.isDirectory) {
        Some(dir)
      } else {
        None
      }
    }
    def getFilesAsString(dir: File): List[String] = {
      val strings = dir.listFiles()
      strings.toList.sorted
        .map(f => Source.fromFile(f, "UTF-8").mkString)
    }
    def toStep(str: String): Step = {
      val tuple = extract(str)
      val header = parseHeader(tuple._1)
      val content = parseBody(tuple._2)
      Step(
        header._1,
        header._2,
        header._3,
        Location(
          header._4._1,
          header._4._2
        ),
        content,
        header._5
      )
    }
    def parseBody(str: String): String = {
      import com.vladsch.flexmark.ast.Node
      import com.vladsch.flexmark.ext.tables.TablesExtension
      import com.vladsch.flexmark.html.HtmlRenderer
      import com.vladsch.flexmark.parser.Parser
      import com.vladsch.flexmark.util.options.MutableDataSet

      val options = new MutableDataSet
      import java.util
      options.set(Parser.EXTENSIONS, util.Arrays.asList(TablesExtension.create))

      val parser: Parser = Parser.builder(options).build();
      val document: Node = parser.parse(str);
      val renderer: HtmlRenderer = HtmlRenderer.builder(options).build();
      renderer.render(document)
    }
    def parseHeader(str: String): (String, String, Int, (Double, Double), List[String]) = {
      import io.circe.yaml.parser;
      val json = (parser.parse(str) match {
        case Right(j) => j
        case _ => throw new Exception()
      }).hcursor

      (
        json.get[String]("name").getOrElse(  ""),
        json.get[String]("slug").getOrElse(  ""),
        json.get[Int]("order").getOrElse(  0),
        (json.field("position").get[Double]("lat").getOrElse(0), json.field("position").get[Double]("lng").getOrElse(0)),
        json.get[List[String]]("pictures").getOrElse(Nil)
      )
    }
    def extract(str: String): (String, String) = {
      val firstOccurence = str.indexOf("------")
      val secondOccurence = str.indexOf("------", firstOccurence + 1)
      (str.substring(firstOccurence + 6, secondOccurence), str.substring(secondOccurence + 6))
    }
    val resourcePath = "trip/steps"

    getDirectory(resourcePath) match {
      case Some(dir) => {
        val steps = getFilesAsString(dir).map(toStep _).sortBy(s => s.order)
        Some(new Trip(steps))
      }
      case _ => None
    }
  }


}
