
Thanks to everyone that showed interest at the 19th International C. elegans Meeting!

In addition to the example videos, please checkout the PDF manuals that come with the software.

Feel free to contact me at [brad.t.moore@duke.edu](mailto:brad.t.moore@duke.edu)
if you have any questions or problems with WormSizer.  Please put WormSizer in the
subject of the email.

## Installation Video Tutorial
View the screencast [here] (http://people.duke.edu/~bm93/wormsizer-install.swf) (flash required).

## WormSizer Demo Video
View the screencast [here] (http://people.duke.edu/~bm93/screencast-example_data.swf)

##To install:
1.  [Download Fiji](http://fiji.sc/Downloads)
2.  Install Fiji
3.  [Download WormSizer](release.zip)
4.  Unzip release.zip
5.  There is documentation, sample data, and the WormSizer plugin in release.zip
6.  Open Fiji, goto Plugins -> Install New PlugIn... and select the file 
    WormSizer-fiji_.jar in the release/ folder.
7.  WormSizer is now installed in Fiji -> Plugins -> WormSizer -> WormSizer GUI


##To build:
1.  Install Maven
2.  Cd into project directory
3.  Run mvn -D skipTests=true assembly:assembly
4.  The *fiji_.jar is the Fiji plugin.

