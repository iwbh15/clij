package net.haesleinhuepf.clij.macro.modules;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJHandler;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij.utilities.AffineTransform;
import net.imglib2.realtransform.AffineTransform3D;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_rotate3D")
public class Rotate3D extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        float angleX = (float)(-asFloat(args[2]) / 180.0f * Math.PI);
        float angleY = (float)(-asFloat(args[3]) / 180.0f * Math.PI);
        float angleZ = (float)(-asFloat(args[4]) / 180.0f * Math.PI);
        boolean rotateAroundCenter = asBoolean(args[5]);

        AffineTransform3D at = new AffineTransform3D();
        Object[] args = openCLBufferArgs();

        if (rotateAroundCenter) {
            ClearCLBuffer input = (ClearCLBuffer) args[0];
            at.translate(-input.getWidth() / 2, -input.getHeight() / 2, -input.getDepth() / 2);
        }
        at.rotate(0, angleX);
        at.rotate(1, angleY);
        at.rotate(2, angleZ);
        if (rotateAroundCenter) {
            ClearCLBuffer input = (ClearCLBuffer) args[0];
            at.translate(input.getWidth() / 2, input.getHeight() / 2, input.getDepth() / 2);
        }

        //boolean result = Kernels.affineTransform(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), AffineTransform.matrixToFloatArray(at));
        //releaseBuffers(args);
        if (clij.getOpenCLVersion() < 1.2) {
            ClearCLBuffer input = ((ClearCLBuffer) args[0]);
            ClearCLBuffer output = ((ClearCLBuffer) args[1]);

            return Kernels.affineTransform3D(clij, input, output, AffineTransform.matrixToFloatArray(at));

        } else {

            ClearCLImage input = CLIJHandler.getInstance().getChachedImageByBuffer((ClearCLBuffer) args[0]);
            ClearCLImage output = CLIJHandler.getInstance().getChachedImageByBuffer((ClearCLBuffer) args[1]);

            boolean result = Kernels.affineTransform3D(clij, input, output, AffineTransform.matrixToFloatArray(at));

            Kernels.copy(clij, output, (ClearCLBuffer) args[1]);


            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number angleX, Number angleY, Number angleZ, Boolean rotateAroundCenter";
    }

    @Override
    public String getDescription() {
        return "Rotates an image stack in 3D. All angles are entered in degrees. If the image is not rotated around \n" +
                "the center, it is rotated around the coordinate origin.\n\n" +
                "It is recommended to apply the rotation to an isotropic image stack.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
