package net.haesleinhuepf.imagej.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import net.haesleinhuepf.imagej.kernels.Kernels;
import net.haesleinhuepf.imagej.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.imagej.macro.CLIJMacroPlugin;
import net.haesleinhuepf.imagej.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.imagej.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_mean2DBox")
public class Mean2DBox extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        int radiusX = asInteger(args[2]);
        int radiusY = asInteger(args[3]);

        if (containsCLBufferArguments()) {
            boolean result = Kernels.meanSeparable(clij, (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]), radiusX, radiusY, 0);
            return result;
        } else {
            return Kernels.meanSeparable(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), radiusX, radiusY, 0);
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number radiusX, Number radiusY";
    }

    @Override
    public String getDescription() {
        return "Computes the local mean average of a pixels rectangular neighborhood. The rectangles size is specified by \n" +
                "its half-width and half-height (radius).";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }

}