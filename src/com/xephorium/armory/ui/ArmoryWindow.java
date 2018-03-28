package com.xephorium.armory.ui;

import com.xephorium.armory.model.ColorProfile;
import com.xephorium.armory.repository.MockColorProfileRepository;
import com.xephorium.armory.ui.resource.color.ArmoryColor;
import com.xephorium.armory.ui.resource.dimension.ArmoryDimension;
import com.xephorium.armory.ui.utility.DialogFactory;
import com.xephorium.armory.ui.utility.DirectoryChooser;
import com.xephorium.armory.ui.utility.DisplayUtility;
import com.xephorium.armory.ui.InstallDirectoryPanel.InstallDirectoryPanelListener;
import com.xephorium.armory.ui.utility.DirectoryChooser.DirectoryChooserListener;
import com.xephorium.armory.ui.FactionConfigurationPanel.FactionConfigurationPanelListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


/*
 * Chris Cruzen                                March 2018
 * Armory Window
 *
 *   ArmoryWindow is the single, top-layer view class of
 * Halo Wars Armory. Its role is to take care of all GUI
 * configuration/display behavior and alert the presenter
 * layer when an action has taken place by ActionListener.
 *
 *   View classes created and managed by ArmoryWindow
 * should remain as near to stateless as possible.
 *
 */


public class ArmoryWindow implements
        InstallDirectoryPanelListener,
        DirectoryChooserListener,
        FactionConfigurationPanelListener {


    /*--- Variables ---*/

    private final int WINDOW_HEIGHT = 500;
    private final int WINDOW_WIDTH = 665;
    private final String WINDOW_TITLE = "  Halo Wars Armory";

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

        ColorProfile[] colorProfileList = MockColorProfileRepository.getProfileList();

        factionConfigurationPanel.updateColorProfiles(colorProfileList);
        profileConfigurationPanel.setColorProfileList(colorProfileList);
    }


    /*--- Public Methods --*/

    public void displayWindow() {
        frame.setVisible(true);
    }

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
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setLocation(DisplayUtility.getWindowStartX(WINDOW_WIDTH), DisplayUtility.getWindowStartY(WINDOW_HEIGHT));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
    }

    private void initializeViewClasses() {
        installDirectoryPanel = new InstallDirectoryPanel(this);
        factionConfigurationPanel = new FactionConfigurationPanel(this);
        profileConfigurationPanel = new ProfileConfigurationPanel();
        profilePreviewPanel = new ProfilePreviewPanel();
        directoryChooser = new DirectoryChooser(this);
    }

    private JPanel createProfilePanels() {
        JPanel eastPanel = new JPanel(new GridLayout(2, 1, 0, ArmoryDimension.PANEL_PADDING));
        eastPanel.setBorder(new EmptyBorder(
                ArmoryDimension.PANEL_PADDING/2,
                ArmoryDimension.PANEL_PADDING/2,
                ArmoryDimension.WINDOW_PADDING_VERTICAL,
                ArmoryDimension.WINDOW_PADDING_HORIZONTAL));
        eastPanel.setPreferredSize(new Dimension(ArmoryDimension.COLOR_PROFILE_PANEL_WIDTH, 0));
        eastPanel.setBackground(ArmoryColor.WINDOW_BACKGROUND_COLOR);

        eastPanel.add(profileConfigurationPanel);
        eastPanel.add(profilePreviewPanel);
        return eastPanel;
    }


    /*--- Listener Interface ---*/

    public interface ArmoryWindowListener {

        void handleBrowseButtonClick();

        void handleDirectorySelection(String directory);

    }


    /*--- Interface Overrides ---*/

    @Override
    public void handleBrowseButtonClick() {
        listener.handleBrowseButtonClick();
    }

    @Override
    public void handleDirectorySelection(String directory) {
        listener.handleDirectorySelection(directory);
    }

    @Override
    public void handleUNSCPlayerUpdate(int playerNumber, String profileName) {
        // TODO - Update UNSC Player
    }

    @Override
    public void handleCovenantPlayerUpdate(int playerNumber, String profileName) {
        // TODO - Update Covenant Player
    }

    @Override
    public void handleUNSCConfigurationReset() {
        // TODO - Reset UNSC Configuration
    }

    @Override
    public void handleCovenantConfigurationReset() {
        // TODO - Reset Covenant Configuration
    }

    @Override
    public void handleUNSCConfigurationSave() {
        // TODO - Save UNSC Configuration
    }

    @Override
    public void handleCovenantConfigurationSave() {
        // TODO - Save Covenant Configuration
    }

//    @Override
//    public void handleProfileColorClick(ColorType colorType, Color currentColor) {
//        DialogFactory.createColorChooserDialog(currentColor,new ColorChooser.ColorChooserListener() {
//            @Override
//            public void onColorSelected(Color color) {
//                profileConfigurationPanel.setWorkingProfileColor(colorType, color);
//            }
//
//            @Override
//            public void onDialogClose() {
//                // Do Nothing
//            }
//        });
//    }
//
//    @Override
//    public void handleProfileSaveClick() {
//        // TODO - Save Selected ColorProfile
//    }
}
