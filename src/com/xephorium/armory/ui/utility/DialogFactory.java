package com.xephorium.armory.ui.utility;

import com.xephorium.armory.ui.utility.ColorChooser.ColorChooserListener;
import com.xephorium.armory.ui.resource.image.ArmoryImage;

import javax.swing.*;
import java.awt.*;

public class DialogFactory {

    /*--- Dialogs ---*/

    public static void createGameNotFoundDialog(JFrame frame) {
        JOptionPane.showMessageDialog(frame,
                "No Halo Wars installation found.",
                "Game Not Found",
                JOptionPane.ERROR_MESSAGE,
                ArmoryImage.ICON_INSTALLATION_INVALID_DIALOG);
    }

    public static void createGameFoundDialog(JFrame frame) {
        JOptionPane.showMessageDialog(frame,
                "Installation found!",
                "Game Found",
                JOptionPane.INFORMATION_MESSAGE,
                ArmoryImage.ICON_INSTALLATION_FOUND_DIALOG);
    }

    public static void createColorChooserDialog(Color initialColor, ColorChooserListener listener) {
        ColorChooser colorChooser = new ColorChooser(listener);
        colorChooser.showDialog(initialColor);
    }
}
