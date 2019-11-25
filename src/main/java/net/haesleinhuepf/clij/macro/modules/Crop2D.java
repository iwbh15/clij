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
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_crop2D")
public class Crop2D extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        if (containsCLImageArguments()) {
            return Kernels.crop(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), asInteger(args[2]), asInteger(args[3]));
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.crop(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]), asInteger(args[3]));
            releaseBuffers(args);
            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number startX, Number startY, Number width, Number height";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        int width = asInteger(args[4]);
        int height = asInteger(args[5]);

        return clij.createCLBuffer(new long[]{width, height}, input.getNativeType());
    }

    @Override
    public String getDescription() {
        return "Crops a given rectangle out of a given image.\n\n" +
                "Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }

}
