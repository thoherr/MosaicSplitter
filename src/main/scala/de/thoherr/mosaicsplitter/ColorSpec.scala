package de.thoherr.mosaicsplitter

import collection.immutable.Map
import xml.XML

class ColorSpec(val color : Color, val legoId : Int, val label : String, val name : String, val r : Int, val g : Int, val b : Int, pantone : String) extends HtmlSnippet {
  override def toString = "[" + color.toString + "," + legoId + "," + name + ",(" + r + "," + g + "," + b + ")," + pantone + "]"
  def toColorHexString = "#" + (r * 256 * 256 + g * 256 + b).toHexString.formatted("%6s").replace(' ', '0') // TBD: There must be a much better way to do this!
  def textColor = if (r+g+b > 384) "#111111" else "#EEEEEE"

  def toHtmlTextWithBackgroundColor(width : Int, height : Int) = {
    <td align="center" valign="middle">{label}</td> %
      attribute("width", width) % attribute("height", height) %
      attribute("style", "color:" + textColor) % attribute("bgcolor", toColorHexString)
  }

  def toHtmlBackgroundColor(width : Int, height : Int) = {
    <td/> %
      attribute("width", width) % attribute("height", height) % attribute("bgcolor", toColorHexString)
  }

 }

object ColorSpec {

  private val colorspecstream = getClass.getResourceAsStream("/colorspec.xml")
  private val colorspectable : Map[Color, ColorSpec] = ColorSpecReader.createFromXML(XML.load(colorspecstream))

  private val undefined = new ColorSpec(Color("00_undefined"), 0, "U", "UNDEFINIERT", 127, 127, 127, "UNDEFINIERT")

  def isDefinedFor(color : Color) = colorspectable.contains(color)

  def apply(color : Color) = if (colorspectable.contains(color)) colorspectable(color) else undefined

}