package com.xephorium.armory.ui.utility;

import com.xephorium.armory.ui.utility.ColorChooser.ColorChooserListener;
import com.xephorium.armory.ui.utility.VerifyActionDialog.VerifyActionListener;
import com.xephorium.armory.ui.resource.image.ArmoryImage;

import javax.swing.*;
import java.awt.*;

public class DialogFactory {


    /*--- Variables ---*/

    private static final String TITLE_LEFT_PADDING = " ";

    /*--- Dialogs ---*/

    public static void createGameNotFoundDialog(JFrame frame) {
        JOptionPane.showMessageDialog(frame,
                "No Halo Wars installation found.",
                TITLE_LEFT_PADDING + "Game Not Found",
                JOptionPane.ERROR_MESSAGE,
                ArmoryImage.ICON_FAILURE_DIALOG);
    }

    public static void createGameFoundDialog(JFrame frame) {
        JOptionPane.showMessageDialog(frame,
                "Installation found!",
                TITLE_LEFT_PADDING + "Game Found",
                JOptionPane.INFORMATION_MESSAGE,
                ArmoryImage.ICON_SUCCESS_DIALOG);
    }

    public static void createProfileMustHaveNameDialog(JFrame frame) {
        JOptionPane.showMessageDialog(frame,
                "Profile must have a name.",
                TITLE_LEFT_PADDING + "Invalid Name",
                JOptionPane.ERROR_MESSAGE,
                ArmoryImage.ICON_FAILURE_DIALOG);
    }

    public static void createProblemSavingDialog(JFrame frame) {
        JOptionPane.showMessageDialog(frame,
                "There was a problem saving.",
                TITLE_LEFT_PADDING + "Changes Not Saved",
                JOptionPane.INFORMATION_MESSAGE,
                ArmoryImage.ICON_FAILURE_DIALOG);
    }

    public static void createProfileSavedDialog(JFrame frame) {
        JOptionPane.showMessageDialog(frame,
                "Your profile has been saved.",
                TITLE_LEFT_PADDING + "Profile Saved",
                JOptionPane.INFORMATION_MESSAGE,
                ArmoryImage.ICON_SUCCESS_DIALOG);
    }

    public static void createConfigurationSavedDialog(JFrame frame) {
        JOptionPane.showMessageDialog(frame,
                "Player colors have been saved.",
                TITLE_LEFT_PADDING + "Configuration Saved",
                JOptionPane.INFORMATION_MESSAGE,
                ArmoryImage.ICON_SUCCESS_DIALOG);
    }

    public static void createNoChangesToSaveDialog(JFrame frame) {
        JOptionPane.showMessageDialog(frame,
                "There are no changes to save.",
                TITLE_LEFT_PADDING + "No Changes to Save",
                JOptionPane.INFORMATION_MESSAGE,
                ArmoryImage.ICON_SUCCESS_DIALOG);
    }

    public static void createNoInstallationSetDialog(JFrame frame) {
        JOptionPane.showMessageDialog(frame,
                "No game installation set.\nClick 'Browse' to get started.",
                TITLE_LEFT_PADDING + "No Installation",
                JOptionPane.INFORMATION_MESSAGE,
                ArmoryImage.ICON_UNKNOWN_DIALOG);
    }

    public static void createDeleteProfileDialog(VerifyActionListener listener, String profileName) {
        VerifyActionDialog verifyActionDialog = new VerifyActionDialog(listener,
                TITLE_LEFT_PADDING + "Delete Profile",
                "Delete \"" + profileName + "\"?");
        verifyActionDialog.showDialog();
    }

    public static void createColorChooserDialog(Color initialColor, ColorChooserListener listener) {
        ColorChooser colorChooser = new ColorChooser(listener);
        colorChooser.showDialog(initialColor);
    }
}
