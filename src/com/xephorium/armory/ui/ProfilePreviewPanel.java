package com.xephorium.armory.ui;

import com.mortennobel.imagescaling.ResampleOp;
import com.xephorium.armory.ui.resource.color.ArmoryColor;
import com.xephorium.armory.ui.resource.dimension.ArmoryDimension;
import com.xephorium.armory.ui.resource.image.ArmoryImage;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ProfilePreviewPanel extends JPanel {


    /*--- Variables ---*/

    private static final int PREVIEW_HEIGHT = ArmoryDimension.PREVIEW_PANEL_HEIGHT;
    private static final int PREVIEW_WIDTH = ArmoryDimension.PREVIEW_PANEL_WIDTH;

    ResampleOp imageResampler;
    private BufferedImage backgroundImage;
    private BufferedImage hudImage;


    /*--- Constructor ---*/

    public ProfilePreviewPanel() {
        super();

        initializeGraphicsUtilities();
        initializeImages();

        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createLineBorder(ArmoryColor.WINDOW_BORDER_COLOR_LIGHT));
        this.setPreferredSize(new Dimension(0, ArmoryDimension.PREVIEW_PANEL_HEIGHT));
    }


    /*--- Public Methods  ---*/

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        if (backgroundImage != null) {
            graphics.drawImage(rescaleImage(backgroundImage), 0, 0, null);
            graphics.drawImage(rescaleImage(tintImage(hudImage, new Color(37, 118, 181))), 0, 0, null);
        }
    }


    /*--- Private Rendering Methods ---*/

    // 1. Get Alpha Image From ArmoryImage
    // 2. Create BufferedImage of Specified Color
    // 3. Add Alpha to BufferedImage
    // 4. Run BufferedImage Through Scale Method

    private BufferedImage rescaleImage(BufferedImage bufferedImage) {
        return imageResampler.filter(bufferedImage, null);
    }

    private BufferedImage tintImage(BufferedImage inputImage, Color color) {
        return maskBufferedImageWithAlpha(createColoredBufferedImage(inputImage, color), inputImage);
    }

    private BufferedImage createColoredBufferedImage(BufferedImage inputImage, Color color) {
        BufferedImage newBufferedImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = newBufferedImage.createGraphics();

        graphics.setPaint(color);
        graphics.fillRect(0, 0, newBufferedImage.getWidth(), newBufferedImage.getHeight());
        return newBufferedImage;
    }

    private BufferedImage maskBufferedImageWithAlpha(BufferedImage inputImage, BufferedImage outputImage) {
        final int width = inputImage.getWidth();
        int[] imgData = new int[width];
        int[] maskData = new int[width];

        for (int y = 0; y < inputImage.getHeight(); y++) {

            // fetch a line of data from each image
            inputImage.getRGB(0, y, width, 1, imgData, 0, 1);
            outputImage.getRGB(0, y, width, 1, maskData, 0, 1);

            // apply the mask
            for (int x = 0; x < width; x++) {
                int color = imgData[x] & 0x00FFFFFF; // mask away any alpha present
                int maskColor = (maskData[x] & 0x00FF0000) << 8; // shift red into alpha bits
                color |= maskColor;
                imgData[x] = color;
            }

            // replace the data
            inputImage.setRGB(0, y, width, 1, imgData, 0, 1);
        }
        return inputImage;
    }

    /*--- Private Setup Methods ---*/

    private void initializeGraphicsUtilities() {
        imageResampler = new ResampleOp (PREVIEW_WIDTH, PREVIEW_HEIGHT);
    }

    private void initializeImages() {
        backgroundImage = ArmoryImage.PREVIEW_BACKGROUND;
        hudImage = ArmoryImage.PREVIEW_MASK_HUD;
    }
}
