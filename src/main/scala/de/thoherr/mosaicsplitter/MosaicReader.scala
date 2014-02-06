package de.thoherr.mosaicsplitter

import scala.xml.{Node, Elem}

object MosaicReader {
  def createFromXML(xmlSpec: Elem) = {
    val getColorText = (unit: Node) => new Part(new Element((unit \\ "element").text.trim), Color((unit \\ "color").text.trim))
    val getRow = (row: Node) => (row \\ "unit").map(getColorText).toArray
    new Mosaic((xmlSpec \\ "row").map(getRow).toArray)
  }
}
