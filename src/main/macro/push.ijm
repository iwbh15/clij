// ClearCLIJ example macro: mean.ijm
//
// This macro shows how the mean average filter works in the GPU.
//
// Author: Robert Haase
// December 2018
// ---------------------------------------------


// Get test data
// run("T1 Head (2.4M, 16-bits)");
newImage("Untitled", "16-bit black", 256, 256, 128);
rename("Mean CPU")
input = getTitle();
getDimensions(width, height, channels, slices, frames);

// create an emtpy image to put the blurred pixels in
newImage("Untitled", "16-bit black", width, height, slices);
rename("Mean GPU");
blurred = getTitle();

// Init GPU
run("CLIJ Macro Extensions", "cl_device=gfx902");
Ext.CLIJ_clear();

// push images to GPU
Ext.CLIJ_push(input);