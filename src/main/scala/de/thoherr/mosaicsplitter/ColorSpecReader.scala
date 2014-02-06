package de.thoherr.mosaicsplitter

import scala.xml.{Node, Elem, XML}

object ColorSpecReader {

  def createFromXML(xmlSpec: Elem) = {
    val extractColorSpec =
      (colorSpec: Node) => {
        val color = Color((colorSpec \\ "color").text)
        (color -> new ColorSpec(color,
          (colorSpec \\ "legoId").text.toInt,
          (colorSpec \\ "label").text,
          (colorSpec \\ "name").text,
          (colorSpec \\ "r").text.toInt,
          (colorSpec \\ "g").text.toInt,
          (colorSpec \\ "b").text.toInt,
          (colorSpec \\ "pantone").text))
      }
    (xmlSpec \\ "colorspec").map(extractColorSpec).toMap[Color, ColorSpec]
  }

}