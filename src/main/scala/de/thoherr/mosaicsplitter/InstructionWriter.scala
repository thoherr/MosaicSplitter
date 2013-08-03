package de.thoherr.mosaicsplitter

import xml.{Text, XML, Null, Attribute}

object InstructionWriter extends HtmlSnippet {

  val detailTileSize = 40
  val overviewTileSize = 5

  val generator = "MosaicSplitter " + MosaicSplitter.version + " - (c) 2011, 2013 Thomas Herrmann"
  val copyright = "(c) 2013 Bricking Bavaria e.V."
  
  def header(title : String) =
    <head>
      <title>{title}</title>
      <meta name="generator" content={generator}/>
      <meta name="copyright" content={copyright}/>
      <meta http-equiv="Content-Type" content="text/html"/>
      <link rel="stylesheet" type="text/css" href="mosaic.css" />
    </head>

  def writeOverviewInstructions(mosaic : Mosaic, mosaicName : String) {
    writeOverviewInstructionsToFile(mosaic, mosaicName, overviewTileSize, "Gesamt.html", "Gesamtmosaik")
  }

  def writeOverviewInstructionsToFile(mosaic : Mosaic, mosaicName : String, tileSize : Int, fileName : String, title : String = "Mosaikteil", cellspacing : Int = 1, cellpadding : Int = 0, border : Int = 0) {
    val fileContent = <html>
      {header(title)}
      <body>
        <h1>{mosaicName}</h1>
        <h2>{title}</h2>
        {mosaic.toGraphicsHtmlTable(tileSize, cellspacing, cellpadding, border)}
        <h2>Benötigte Teile</h2>
        <p>{mosaic.toPartListHtmlTable(2, 10, 1)}</p>
      </body>
      </html>
    XML.save(fileName,fileContent)
  }

  def writeModuleInstructions(parts : Array[Array[Mosaic]], mosaicName : String) {
    val rows = parts.length
    val cols = parts(0).length
    for (rowNum <- 0 until rows)
      for (colNum <- 0 until cols) {
        val moduleRow : Int = rowNum/2 + 1
        val moduleY = if ( rowNum % 2 == 1 ) "unten" else "oben"
        val moduleCol : Int = colNum/2 + 1
        val moduleX = if ( colNum % 2 == 1 ) "rechts" else "links"
        val moduleNumber = (moduleRow-1) * cols/2 + moduleCol
        val fileName = "M-" + moduleNumber.formatted("%03d") + "_Z-" + moduleRow.formatted("%02d") + "_S-" + moduleCol.formatted("%02d") + "_" + moduleX(0) + moduleY(0) + ".html"
        val title = "Mosaikteil " + moduleNumber + " (" + moduleX(0) + moduleY(0) + ") - Zeile " + moduleRow + ", Spalte " + moduleCol + ", " + moduleX + " " + moduleY +""
        InstructionWriter.writeModuleInstructionsToFile(parts(rowNum)(colNum), mosaicName, detailTileSize, fileName, title, 0, 1, 1)
      }
  }

  def writeModuleInstructionsToFile(mosaic : Mosaic, name : String, tileSize : Int, fileName : String, title : String = "Mosaikteil", cellspacing : Int = 1, cellpadding : Int = 0, border : Int = 0) {
    val pictureCell = <td rowspan="2" align="right">{mosaic.toTextHtmlTable(tileSize, cellspacing, cellpadding, border)}</td> % Attribute(None, "valign", Text("top"), Null)
    val partListCell = <td>{mosaic.toPartListHtmlTable(tileSize - 20, cellspacing + 5, cellpadding - 2, 0)}</td> % Attribute(None, "valign", Text("top"), Null)
    val fileContent = <html>
      {header(title)}
      <body>
        <table>
          <tr>
            <td rowspan="2" valign="top"><img src="logo.jpg" width="256px"/></td>
            <td>&nbsp;&nbsp;&nbsp;</td>
            <td align="right" class="titleline">{name}</td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td align="right" class="title"><h2>{title}</h2></td>
          </tr>
          <tr>
            <td><h3>Benötigte Teile</h3></td>
            <td>&nbsp;&nbsp;&nbsp;</td>
            {pictureCell}
          </tr>
          <tr>
            {partListCell}
            <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
          </tr>
          <tr>
            <td colspan="3" align="right" class="copyright">{copyright}</td>
          </tr>
          <tr>
            <td colspan="3" align="left" class="please_return">Bitte die Bauanleitung zusammen mit der bebauten Grundplatte wieder zur&uuml;ckgeben!</td>
          </tr>
        </table>
      </body>
      </html>
    XML.save(fileName,fileContent)
  }

  def writeColorLabels {
    Color.colorTable.values.foreach(writeColorLabelToFile)
  }

  def writeColorLabelToFile(color : Color) {
    val colorCell = <tr class="colorLabel">{ColorSpec(color).toHtmlTextWithBackgroundColor(400, 400)}<tr><td class="partlist">{ColorSpec(color).name}</td></tr></tr>
    val fileContent = <html>
      {header("Farbtafel")}
      <body>
        {table(colorCell)}
      </body>
      </html>
    val fileName = "Color_" + color.label + ".html"
    XML.save(fileName,fileContent)
  }

}