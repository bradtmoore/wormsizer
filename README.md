##New Installation Instructions (how to get the latest version)
1.  If you previously installed WormSizer by copying the .jar to the Fiji directory,
you must first remove the old plugin.
    a.  Delete Fiji.app/plugins/WormSizer_-fiji.jar
    b.  Restart Fiji
2.  Open Fiji
3.  Goto Help->Update Fiji
4.  Click on Manage Update Sites
5.  Scroll to the bottom, and check WormSizer
6.  Click Add and then Close
7.  Files for WormSizer should now appear in the update screen
8.  Click Apply Changes
9.  Done, you will now have the latest version of WormSizer and will receive automatic update notifications
every time you start Fiji.

##Version 1.2.0 - Surface Area
WormSizer now calculates surface area of nematodes.  The method is analogous to the volume calculation;
each worm is considered a collection of frustrums of cones and the surface area and volume are the sum
of these measurements.  The measurement is reported as microns squared.

To add the surface area measurement to existing results, a script has been included in the WormSizer plugin.

1.  Make a backup of the original files
2.  Make sure you have the latest version of WormSizer installed in Fiji
3.  Open the command-line (Mac OS and Unix)
4.  The script command is java -cp /Applications/Fiji.app/jars/opencsv-2.0.jar:/Applications/Fiji.app/plugins/WormSizer_-1.2.0.jar edu.duke.biology.baughlab.wormsize.AddSurfaceArea [FILES]
   a.  [FILES] is a list of .xml files output by WormSizer (the .CSV files will be detected and updated as well).
   b.  Replace /Applications/Fiji.app with your Fiji.app location if it is different.
5.  The script will display each file that has been updated.

Thanks to everyone that showed interest at the 19th International C. elegans Meeting!

In addition to the example videos, please checkout the PDF manuals that come with the software.

Feel free to contact me at [brad.t.moore@duke.edu](mailto:brad.t.moore@duke.edu)
if you have any questions or problems with WormSizer.  Please put WormSizer in the
subject of the email.

## WormSizer Demo Video
View the screencast [here] (http://people.duke.edu/~bm93/screencast-example_data.swf)


