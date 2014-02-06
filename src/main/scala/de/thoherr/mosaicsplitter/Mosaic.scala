package de.thoherr.mosaicsplitter

import scala.xml._

class Mosaic(pictureData: Array[Array[ColoredElement]]) extends HtmlSnippet {

  def info = "Size of mosaic is " + pictureData(0).length + " columns by " + pictureData.length + " rows (total " + pictureData(0).length * pictureData.length + " pieces)."

  /**
   * Splits a mosaic into smaller pieces of given size and returns an Array[Array[Mosaic]]
   */
  def splitIntoPiecesOfSize(x: Int, y: Int) = {
    val plateColumns = pictureData(0).length / x
    val plateRows = pictureData.length / y
    println("Splitting into " + plateColumns + " by " + plateRows + " modules (total " + plateColumns * plateRows + ", each " + x + " * " + y + " = " + x * y + " pieces)")
    val result: Array[Array[Mosaic]] = Array.ofDim(plateRows, plateColumns)
    for (row <- 0 until plateRows) {
      val rowSlice = pictureData.slice(row * y, (row + 1) * y)
      for (col <- 0 until plateColumns) {
        val plate = rowSlice.map((rowData: Array[ColoredElement]) => rowData.slice(col * x, (col + 1) * x))
        result(row)(col) = new Mosaic(plate)
      }
    }
    result
  }

  /**
   * creates a (sorted) list of tuples containing the number of pieces for each color used in the picture
   */
  def getSortedPartList = {
    val allColoredElements: List[ColoredElement] = pictureData.toList.flatten
    val partList = allColoredElements groupBy (_.color) map {
      case (k, v) => k -> v.size
    }
    partList.toList.sortWith((p1, p2) => p1._1 < p2._1)
  }

  def toPartListHtmlTable(tileSize: Int, cellspacing: Int = 1, cellpadding: Int = 0, border: Int = 0) = {
    <table>
      {getSortedPartList map {
      case (color, count) =>
        <tr class="partlist">
          <td align="right">{count}</td>
          <td>mal</td>
          <td>{ColorSpec(color).toHtmlTextWithBackgroundColor(tileSize, tileSize)}</td>
          <td>{color.toString}</td>
        </tr>
    }}
    </table> %
      attribute("cellspacing", cellspacing) %
      attribute("cellpadding", cellpadding) %
      attribute("border", border)
  }

  def toGraphicsHtmlTable(tileSize: Int, cellspacing: Int = 1, cellpadding: Int = 0, border: Int = 0) = {
    toHtmlTable((col: ColoredElement) => ColorSpec(col.color).toHtmlBackgroundColor(tileSize, tileSize), cellspacing, cellpadding, border)
  }

  def toTextHtmlTable(tileSize: Int, cellspacing: Int = 1, cellpadding: Int = 0, border: Int = 0) = {
    toHtmlTable((col: ColoredElement) => ColorSpec(col.color).toHtmlTextWithBackgroundColor(tileSize, tileSize), cellspacing, cellpadding, border)
  }

  private def toHtmlTable(cellContent: ColoredElement => Elem, cellspacing: Int = 1, cellpadding: Int = 0, border: Int = 0) = {
    val col2html = cellContent
    val row2html = (row: Array[ColoredElement]) => <tr>
      {row.map((c: ColoredElement) => {
        col2html(c)
      })}
    </tr>: Elem
    table(pictureData.map(row2html), cellspacing, cellpadding, border)
  }
}
