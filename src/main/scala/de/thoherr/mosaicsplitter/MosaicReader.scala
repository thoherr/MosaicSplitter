package de.thoherr.mosaicsplitter

import scala.xml.{Node, Elem}

object MosaicReader {
  def createFromXML(xmlSpec: Elem) = {
    val getColoredElement = (unit: Node) => new ColoredElement(new Element((unit \\ "element").text.trim), Color((unit \\ "color").text.trim))
    val getRow = (row: Node) => (row \\ "unit").map(getColoredElement).toArray
    new Mosaic((xmlSpec \\ "row").map(getRow).toArray)
  }
}
