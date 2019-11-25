package net.haesleinhuepf.clij.macro.modules;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import ij.IJ;
import ij.ImagePlus;
import ij.plugin.Duplicator;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class Blur2DTest {
    @Test
    public void blur2d() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testFlyBrain3D = IJ.openImage("src/test/resources/flybrain.tif");
        ImagePlus testFlyBrain2D = new Duplicator().run(testFlyBrain3D, 1, 1);

        // do operation with ImageJ
        ImagePlus gauss = new Duplicator().run(testFlyBrain2D);
        ImagePlus gaussCopy = new Duplicator().run(testFlyBrain2D);
        IJ.run(gauss, "Gaussian Blur...", "sigma=2");

        // do operation with ClearCL
        ClearCLImage src = clij.convert(gaussCopy, ClearCLImage.class);
        ClearCLImage dst = clij.createCLImage(src);

        Kernels.blur(clij, src, dst, 2f, 2f);
        ImagePlus gaussFromCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(gauss, gaussFromCL, 2));

        src.close();
        dst.close();
        IJ.exit();
        clij.close();
    }

    @Test
    public void blur2d_Buffers() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testFlyBrain3D = IJ.openImage("src/test/resources/flybrain.tif");
        ImagePlus testFlyBrain2D = new Duplicator().run(testFlyBrain3D, 1, 1);

        // do operation with ImageJ
        ImagePlus gauss = new Duplicator().run(testFlyBrain2D);
        ImagePlus gaussCopy = new Duplicator().run(testFlyBrain2D);
        IJ.run(gauss, "Gaussian Blur...", "sigma=2");

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(gaussCopy, ClearCLBuffer.class);
        ClearCLBuffer dst = clij.createCLBuffer(src);

        Kernels.blur(clij, src, dst, 2f, 2f);
        ImagePlus gaussFromCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(gauss, gaussFromCL, 2));

        src.close();
        dst.close();
        IJ.exit();
        clij.close();
    }

}