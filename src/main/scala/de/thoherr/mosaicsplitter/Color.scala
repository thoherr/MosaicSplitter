package de.thoherr.mosaicsplitter

import scala.collection.mutable.HashMap

class Color private (val name : String) extends Ordered[Color] {

  def legoId = if (ColorSpec.isDefinedFor(this)) ColorSpec(this).legoId else name.substring(0,2).toInt
  def label = if (ColorSpec.isDefinedFor(this)) ColorSpec(this).label else name
  override def toString = if (ColorSpec.isDefinedFor(this)) ColorSpec(this).name else name.substring(3)
  def compare(that : Color) = label.compare(that.label)

  // These two methods are necessary in order to use Color as Key to Maps etc.
  override def equals(other : Any) = {
    other != null && other.isInstanceOf[Color] && name.equalsIgnoreCase(other.asInstanceOf[Color].name)
  }
  override def hashCode() = name.hashCode()
}

object Color {
  val colorTable = new HashMap[String, Color]
  def apply(name : String) = colorTable.getOrElseUpdate(name.trim.toLowerCase, new Color(name.trim.toLowerCase))
}
