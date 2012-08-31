CellProfiler Pipeline: http://www.cellprofiler.org
Version:1
SVNRevision:11710

LoadImages:[module_num:1|svn_version:\'11587\'|variable_revision_number:11|show_window:True|notes:\x5B\x5D]
    File type to be loaded:individual images
    File selection method:Text-Exact match
    Number of images in each group?:3
    Type the text that the excluded images have in common:Do not use
    Analyze all subfolders within the selected folder?:None
    Input image file location:Default Input Folder\x7CNone
    Check image sets for missing or duplicate files?:Yes
    Group images by metadata?:No
    Exclude certain files?:No
    Specify metadata fields to group by:
    Select subfolders to analyze:
    Image count:1
    Text that these images have in common (case-sensitive):tif
    Position of this image in each group:1
    Extract metadata from where?:None
    Regular expression that finds metadata in the file name:^(?P<Plate>.*)_(?P<Well>\x5BA-P\x5D\x5B0-9\x5D{2})_s(?P<Site>\x5B0-9\x5D)
    Type the regular expression that finds metadata in the subfolder path:.*\x5B\\\\/\x5D(?P<Date>.*)\x5B\\\\/\x5D(?P<Run>.*)$
    Channel count:1
    Group the movie frames?:No
    Grouping method:Interleaved
    Number of channels per group:3
    Load the input as images or objects?:Images
    Name this loaded image:Raw
    Name this loaded object:Nuclei
    Retain outlines of loaded objects?:No
    Name the outline image:LoadedImageOutlines
    Channel number:1
    Rescale intensities?:No

ApplyThreshold:[module_num:2|svn_version:\'6746\'|variable_revision_number:5|show_window:True|notes:\x5B\x5D]
    Select the input image:Raw
    Name the output image:Seg
    Select the output image type:Binary (black and white)
    Set pixels below or above the threshold to zero?:Below threshold
    Subtract the threshold value from the remaining pixel intensities?:No
    Number of pixels by which to expand the thresholding around those excluded bright pixels:0.0
    Select the thresholding method:Otsu Global
    Manual threshold:0.0
    Lower and upper bounds on threshold:0.000000,1.000000
    Threshold correction factor:1
    Approximate fraction of image covered by objects?:0.01
    Select the input objects:None
    Two-class or three-class thresholding?:Two classes
    Minimize the weighted variance or the entropy?:Weighted variance
    Assign pixels in the middle intensity class to the foreground or the background?:Foreground
    Select the measurement to threshold with:None

ImageMath:[module_num:3|svn_version:\'10718\'|variable_revision_number:3|show_window:True|notes:\x5B\x5D]
    Operation:Invert
    Raise the power of the result by:1
    Multiply the result by:1
    Add to result:0
    Set values less than 0 equal to 0?:Yes
    Set values greater than 1 equal to 1?:Yes
    Ignore the image masks?:No
    Name the output image:SegInvert
    Image or measurement?:Image
    Select the first image:Seg
    Multiply the first image by:1
    Measurement:
    Image or measurement?:Image
    Select the second image:
    Multiply the second image by:1
    Measurement:

ApplyThreshold:[module_num:4|svn_version:\'6746\'|variable_revision_number:5|show_window:True|notes:\x5B\x5D]
    Select the input image:SegInvert
    Name the output image:SegInvertBinary
    Select the output image type:Binary (black and white)
    Set pixels below or above the threshold to zero?:Below threshold
    Subtract the threshold value from the remaining pixel intensities?:No
    Number of pixels by which to expand the thresholding around those excluded bright pixels:0.0
    Select the thresholding method:Manual
    Manual threshold:0.0
    Lower and upper bounds on threshold:0.000000,1.000000
    Threshold correction factor:1
    Approximate fraction of image covered by objects?:0.01
    Select the input objects:None
    Two-class or three-class thresholding?:Two classes
    Minimize the weighted variance or the entropy?:Weighted variance
    Assign pixels in the middle intensity class to the foreground or the background?:Foreground
    Select the measurement to threshold with:None

RunImageJ:[module_num:5|svn_version:\'10884\'|variable_revision_number:3|show_window:True|notes:\x5B\x5D]
    Command or macro?:Command
    Command\x3A:WormSizer Batch
    Macro\x3A:run("Invert");
    Options\x3A:xmlfile=\x5BC\x3A\\Users\\Brad\\Desktop\\CellProfilerTest\\results.xml\x5D csvfile=\x5BC\x3A\\Users\\Brad\\Desktop\\CellProfilerTest\\results.csv\x5D micronsperpixel=1.0 minarea=0.0 maxarea=999999.0 minscore=0.0 imagefile=\x5Bdummy-image.tif\x5D experimentid=\x5BmyExperiment\x5D
    Set the current image?:Yes
    Current image\x3A:SegInvertBinary
    Get the current image?:No
    Final image\x3A:ImageJImage
    Wait for ImageJ?:No
    Run before each group?:Nothing
    Command\x3A:None
    Macro\x3A:run("Invert");
    Options\x3A:
    Run after each group?:Nothing
    Command\x3A:None
    Macro\x3A:run("Invert");
    Options\x3A:
    Save the selected image?:No
    Image name\x3A:ImageJGroupImage
    Command settings count:0
    Prepare group command settings count:0
    Post-group command settings count:0
