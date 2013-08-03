package de.thoherr.mosaicsplitter

import collection.immutable.Map
import collection.mutable.HashMap
import xml.{Elem, Node, XML}

class Color private (val name : String) extends Ordered[Color] {

  def legoId = if (ColorSpec.isDefinedFor(this)) ColorSpec(this).legoId else name.substring(0,2).toInt
  def label = if (ColorSpec.isDefinedFor(this)) ColorSpec(this).label else name
  override def toString = if (ColorSpec.isDefinedFor(this)) ColorSpec(this).name else name.substring(3)
  def compare(that : Color) = label.compare(that.label)
  def fileName = name + ".jpg"

  // The next two are necessary in order to use Color as Key to Maps etc.
  override def equals(other : Any) = {
    other != null && other.isInstanceOf[Color] && name.equalsIgnoreCase(other.asInstanceOf[Color].name)
  }
  override def hashCode() = name.hashCode()
}

object Color {
  val colorTable = new HashMap[String, Color]
  def apply(name : String) = colorTable.getOrElseUpdate(name.trim.toLowerCase, new Color(name.trim.toLowerCase))
}

class ColorSpec private (val color : Color, val legoId : Int, val label : String, val name : String, val r : Int, val g : Int, val b : Int, pantone : String) extends HtmlSnippet {
  override def toString = "[" + color.toString + "," + legoId + "," + name + ",(" + r + "," + g + "," + b + ")," + pantone + "]"
  def toHexString = "#" + (r * 256 * 256 + g * 256 + b).toHexString.formatted("%6s").replace(' ', '0') // TBD: There must be a much better way to do this!
  def textColor = if (r+g+b > 384) "#111111" else "#EEEEEE"
  def fileName = color.fileName

  def toHtmlTextWithBackgroundColor(width : Int, height : Int) = {
    <td align="center" valign="middle">{label}</td> %
      attribute("width", width) % attribute("height", height) %
      attribute("style", "color:" + textColor) % attribute("bgcolor", toHexString)
  }

  def toHtmlBackgroundColor(width : Int, height : Int) = {
    <td/> %
      attribute("width", width) % attribute("height", height) % attribute("bgcolor", toHexString)
  }

 }

object ColorSpec {

  private val colorspecstream = getClass.getResourceAsStream("/colorspec.xml")
  private val colorspectable : Map[Color, ColorSpec] = createFromXML(XML.load(colorspecstream))

  private val undefined = new ColorSpec(Color("00_undefined"), 0, "U", "UNDEFINIERT", 127, 127, 127, "UNDEFINIERT")

  def isDefinedFor(color : Color) = colorspectable.contains(color)

  def apply(color : Color) = if (colorspectable.contains(color)) colorspectable(color) else undefined

  private def createFromXML(xmlSpec: Elem) = {
    val extractColorSpec =
      (unit: Node) => {
        val color = Color((unit \\ "color").text)
        (color -> new ColorSpec(color,
          (unit \\ "legoId").text.toInt,
          (unit \\ "label").text,
          (unit \\ "name").text,
          (unit \\ "r").text.toInt,
          (unit \\ "g").text.toInt,
          (unit \\ "b").text.toInt,
          (unit \\ "pantone").text))
      }
    (xmlSpec \\ "colorspec").map(extractColorSpec).toMap[Color, ColorSpec]
  }

}