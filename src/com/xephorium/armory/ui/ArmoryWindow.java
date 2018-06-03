package com.xephorium.armory.ui;

import com.xephorium.armory.model.Profile;
import com.xephorium.armory.model.ProfileList;
import com.xephorium.armory.ui.resource.color.ArmoryColor;
import com.xephorium.armory.ui.resource.dimension.ArmoryDimension;
import com.xephorium.armory.ui.resource.image.ArmoryImage;
import com.xephorium.armory.ui.utility.VerifyActionDialog.VerifyActionListener;
import com.xephorium.armory.ui.utility.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;


/*
 * Chris Cruzen                                March 2018
 * Armory Window
 *
 *   ArmoryWindow is the single, top-layer view class of
 * Halo Wars Armory. Its role is to drive the interface
 * and alert the presenter layer when a user action has
 * taken place.
 *
 *   View classes created and managed by ArmoryWindow
 * should remain as near to stateless as possible.
 *
 */


public class ArmoryWindow {


    /*--- Variables ---*/

    private final String WINDOW_TITLE = "Halo Wars Armory";

    private ArmoryWindowListener listener;

    private JFrame frame;
    private InstallDirectoryPanel installDirectoryPanel;
    private FactionConfigurationPanel factionConfigurationPanel;
    private ProfileConfigurationPanel profileConfigurationPanel;
    private ProfilePreviewPanel profilePreviewPanel;
    private DirectoryChooser directoryChooser;


    /*--- Constructor(s) ---*/

    public ArmoryWindow(ArmoryWindowListener listener) {
        this.listener = listener;

        setGlobalLookAndFeel();
        initializeFrameAttributes();
        initializeViewClasses();
        createProfilePanels();

        frame.add(installDirectoryPanel, BorderLayout.PAGE_START);
        frame.add(factionConfigurationPanel, BorderLayout.CENTER);
        frame.add(createProfilePanels(), BorderLayout.EAST);
    }


    /*--- Core ArmoryWindow Functionality ---*/


    // General

    public void displayWindow() {
        frame.setVisible(true);
    }

    public void updateProfileList(ProfileList profileList) {
        factionConfigurationPanel.updateProfileList(profileList);
        profileConfigurationPanel.updateProfileList(profileList);
    }

    public void displayProblemSavingDialog() {
        DialogFactory.createProblemSavingDialog(frame);
    }


    // Install Directory

    public void displayDirectoryChooser() {
        directoryChooser.displayChooser();
    }

    public void displayGameFoundDialog() {
        DialogFactory.createGameFoundDialog(frame);
    }

    public void displayGameNotFoundDialog() {
        DialogFactory.createGameNotFoundDialog(frame);
    }

    public void setValidInstallDirectory(String directory) {
        installDirectoryPanel.setValidInstallDirectory(directory);
    }

    public void setInvalidInstallDirectory() {
        installDirectoryPanel.setInvalidInstallDirectory();
    }

    public void displayNoInstallationSetDialog() {
        DialogFactory.createNoInstallationSetDialog(frame);
    }


    // Faction Configuration

    public void updateUNSCPlayerConfiguration(List<Integer> unscPlayerProfiles) {
        factionConfigurationPanel.updateUNSCPlayerConfiguration(unscPlayerProfiles);
    }

    public void updateCovenantPlayerConfiguration(List<Integer> covenantPlayerProfiles) {
        factionConfigurationPanel.updateCovenantPlayerConfiguration(covenantPlayerProfiles);
    }

    public void displayConfigurationSavedDialog() {
        DialogFactory.createConfigurationSavedDialog(frame);
    }


    // Profile Configuration

    public void setSelectedProfile(Profile profile) {
        profileConfigurationPanel.setSelectedProfile(profile);
        profileConfigurationPanel.setWorkingProfile(profile);
        profilePreviewPanel.setSelectedProfile(profile);
    }

    public void selectNewProfile(Profile profile) {
        profileConfigurationPanel.selectNewProfile(profile);
        profileConfigurationPanel.setWorkingProfile(profile);
    }

    public void setWorkingProfile(Profile profile) {
        profileConfigurationPanel.setWorkingProfile(profile);
        profilePreviewPanel.setSelectedProfile(profile);
    }

    public void disableConfigurationEdit() {
        factionConfigurationPanel.disableConfigurationEdit();
    }

    public void enableConfigurationEdit() {
        factionConfigurationPanel.enableConfigurationEdit();
    }

    public void disableProfileEdit() {
        profileConfigurationPanel.disableProfileEdit();
    }

    public void enableProfileEdit() {
        profileConfigurationPanel.enableProfileEdit();
    }

    public void displayColorChooserDialog(Color initialColor, ColorChooser.ColorChooserListener listener) {
        DialogFactory.createColorChooserDialog(initialColor, listener);
    }

    public void displayProfileMustHaveNameDialog() {
        DialogFactory.createProfileMustHaveNameDialog(frame);
    }

    public void displayProfileSavedDialog() {
        DialogFactory.createProfileSavedDialog(frame);
    }

    public void displayNoChangesToSaveDialog() {
        DialogFactory.createNoChangesToSaveDialog(frame);
    }

    public void displayDeleteProfileDialog(VerifyActionListener listener, String profileName) {
        DialogFactory.createDeleteProfileDialog(listener, profileName);
    }


    /*--- Private Methods --*/

    private void setGlobalLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // A Whole Lot of Nothin'
        }
    }

    private void initializeFrameAttributes() {
        frame = new JFrame(WINDOW_TITLE);
        frame.setSize(ArmoryDimension.WINDOW_WIDTH, ArmoryDimension.WINDOW_HEIGHT);
        frame.setLocation(
                DisplayUtility.getWindowStartX(ArmoryDimension.WINDOW_WIDTH),
                DisplayUtility.getWindowStartY(ArmoryDimension.WINDOW_HEIGHT));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setIconImages(ArmoryImage.ICON_APPLICATION_MAIN_LIST);
    }

    private void initializeViewClasses() {
        installDirectoryPanel = new InstallDirectoryPanel(listener);
        factionConfigurationPanel = new FactionConfigurationPanel(listener);
        profileConfigurationPanel = new ProfileConfigurationPanel(listener);
        profilePreviewPanel = new ProfilePreviewPanel();
        directoryChooser = new DirectoryChooser(listener);
    }

    private JPanel createProfilePanels() {
        JPanel eastPanel = new JPanel(new BorderLayout());
        eastPanel.setBorder(new EmptyBorder(
                ArmoryDimension.PANEL_PADDING/2,
                ArmoryDimension.PANEL_PADDING/2,
                ArmoryDimension.WINDOW_PADDING_VERTICAL,
                ArmoryDimension.WINDOW_PADDING_HORIZONTAL));
        eastPanel.setPreferredSize(new Dimension(
                ArmoryDimension.PREVIEW_PANEL_WIDTH
                + ArmoryDimension.PANEL_PADDING/2
                + ArmoryDimension.WINDOW_PADDING_HORIZONTAL,
                0));
        eastPanel.setBackground(ArmoryColor.WINDOW_BACKGROUND_COLOR);

        eastPanel.add(profileConfigurationPanel, BorderLayout.CENTER);
        eastPanel.add(profilePreviewPanel, BorderLayout.PAGE_END);
        return eastPanel;
    }
}
