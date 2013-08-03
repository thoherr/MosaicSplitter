package de.thoherr.mosaicsplitter

import xml.{Attribute, Text, Null}

trait HtmlSnippet {

  def attribute(name : String, value : Any) = Attribute(None, name, Text(value.toString), Null)

  def table(content : Any, cellspacing : Int = 1, cellpadding : Int = 0, border : Int = 0) =
    <table>{content}</table> %
      attribute("cellspacing", cellspacing) %
      attribute("cellpadding", cellpadding) %
      attribute("border", border)

}