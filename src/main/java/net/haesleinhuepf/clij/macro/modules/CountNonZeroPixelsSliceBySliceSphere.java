package net.haesleinhuepf.clij.macro.modules;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_countNonZeroPixelsSliceBySliceSphere")
public class CountNonZeroPixelsSliceBySliceSphere extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        if (containsCLImageArguments()) {
            return Kernels.countNonZeroPixelsLocally(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), asInteger(args[2]), asInteger(args[3]));
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.countNonZeroPixelsLocally(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]), asInteger(args[3]));
            releaseBuffers(args);
            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number radiusX, Number radiusY";
    }

    @Override
    public String getDescription() {
        return "Counts non-zero pixels in a sphere around every pixel slice by slice in a stack and puts the resulting number in the destination image stack.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
