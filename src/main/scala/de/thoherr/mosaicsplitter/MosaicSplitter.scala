package de.thoherr.mosaicsplitter

import scala.xml._

object MosaicSplitter {

  val version = "0.4"  // TODO: Unify version info (build.gradle / here / some time a build number) into Version class

  def usage {
    println("Usage: MosaicSplitter <file> <name> [moduleWidth [moduleHeight]]")
    sys.exit(255)
  }

  def main(args : Array[String]) : Unit = {
    if (args.size > 1) {
      val mosaic = Mosaic.createFromXML(XML.load(args(0)))
      val name = args(1)
      val moduleWidth = if (args.size > 2) args(2).toInt else 8
      val moduleHeight = if (args.size > 3) args(3).toInt else moduleWidth
      println(mosaic.info)
      InstructionWriter.writeOverviewInstructions(mosaic, name)
      InstructionWriter.writeModuleInstructions(mosaic.splitIntoPiecesOfSize(moduleWidth, moduleHeight), name)
      InstructionWriter.writeColorLabels
    }
    else
      usage
  }
}
