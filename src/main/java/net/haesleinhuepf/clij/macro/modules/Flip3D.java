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
 * 12 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_flip3D")
public class Flip3D extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        Boolean flipX = asBoolean(args[2]);
        Boolean flipY = asBoolean(args[3]);
        Boolean flipZ = asBoolean(args[4]);

        if (containsCLImageArguments()) {
            return Kernels.flip(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), flipX, flipY, flipZ);
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.flip(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), flipX, flipY, flipZ);
            releaseBuffers(args);
            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Boolean flipX, Boolean flipY, Boolean flipZ";
    }

    @Override
    public String getDescription() {
        return "Flips an image in X, Y and/or Z direction depending on boolean flags.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
