MosaicSplitter
==============

MosaicSplitter creates HTML building instructions for LEGO mosaics.

This program splits the XML-output of the awesome PicToBrick program by Tobias Reichling and Adrian Schütz
(http://www.pictobrick.de/en/pictobrick.shtml) into a bunch of HTML files, giving building instructions
for smaller tiles of the mosaic.

We use LEGO plates sized 16 * 16 studs and put 64 "pixels" (8 * 8) on them. Four of this plates are mounted
on a 32 * 32 base plate which form the mosaic.

You have to instruct PicToBrick to output 1 * 1 tiles or 1 * 1 bricks only, because MosaicSplitter
expects a square pixel structure as input.

MosaicSplitter was originally written for the LEGO mosaic of Bricking Bavaria e. V. at
Bricking Bavaria 2011 in Munich and reused for the LEGO mosaic at Bricking Bavaria 2013.

Usage
-----

Build the jar file with gradle

    gradle jar

Convert the XML of the mosaic and create the HTML instruction pages using the jarfile in build/libs.

    scala <path_to_build_libs>/MosaicSplitter-0.5.jar <inputfile> <title> [moduleWidth [moduleHeight]]

The default for the module size is 8 * 8 LEGO bricks. Since we use 2 * 2 bricks this creates plates
with a size of 16 * 16 studs.

The resulting HTML files can be converted to PDF with the scripts in the bin directory,
using wkhtmltopdf (https://code.google.com/p/wkhtmltopdf/).

Building
--------

You will need the Scala programming language installed on your system.
The software was written in Scala 2.9.0, which was the release available when i
started the project, but the build is against the current release 2.10.3.

The build is done by gradle (http://www.gradle.org/). I currently use version 1.10.

Known bugs and flaws
---------------------

- The Resources (Color Jpegs, logo.jpg, mosaic.css) has to be copied to the destination directory manually (should be written by the program)
- The text in the resulting HTML files is basically hard-coded and in german.

License
-------

MosaicSplitter is licensed under [GNU General Public Licence (GPL) Version 3](http://www.gnu.org/licenses/gpl-3.0.en.html).
