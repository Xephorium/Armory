package com.xephorium.armory.ui;

import com.xephorium.armory.model.ProfileList;
import com.xephorium.armory.ui.ProfileBrowsePanel.ProfileBrowsePanelListener;
import com.xephorium.armory.ui.ProfileAttributePanel.ProfileAttributePanelListener;
import com.xephorium.armory.model.Profile;
import com.xephorium.armory.ui.resource.color.ArmoryColor;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ProfileConfigurationPanel extends JPanel implements
        ProfileAttributePanelListener,
        ProfileBrowsePanelListener {


    /*--- Variables ---*/

    private ArmoryWindowListener listener;
    private ProfileBrowsePanel profileBrowsePanel;
    private ProfileAttributePanel profileAttributePanel;


    /*--- Constructor ---*/

    public ProfileConfigurationPanel(ArmoryWindowListener listener) {
        super();
        this.listener = listener;

        initializePanelAttributes();
        initializeViewClasses();

        this.add(profileBrowsePanel, BorderLayout.CENTER);
        this.add(profileAttributePanel, BorderLayout.LINE_END);
    }


    /*--- Public Methods ---*/

    public void updateProfileList(ProfileList profileList) {
        profileBrowsePanel.updateProfileBrowsePanel(profileList);
    }

    public void setSelectedProfile(Profile profile) {
        profileBrowsePanel.setSelectedProfile(profile);
        profileAttributePanel.setWorkingProfile(profile);
    }

    public void setWorkingProfile(Profile profile) {
        profileAttributePanel.setWorkingProfile(profile);
    }


    /*--- Private Methods ---*/

    private void initializePanelAttributes() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        this.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(ArmoryColor.WINDOW_BORDER_COLOR_LIGHT),
                new EmptyBorder(15, 15, 15, 15)
        ));
    }

    private void initializeViewClasses() {
        profileBrowsePanel = new ProfileBrowsePanel(this);
        profileAttributePanel = new ProfileAttributePanel(this);
    }


    /*--- Interface Overrides ---*/

    @Override
    public void handleProfileSelection(Profile profile) {
        listener.handleProfileSelection(profile);
    }

    @Override
    public void handleWorkingProfileSaveClick(Profile newProfile) {
        listener.handleWorkingProfileSaveClick(newProfile);
    }

    @Override
    public void handleWorkingProfileColorClick(Profile workingProfile, Profile.ColorType colorType) {
        listener.handleWorkingProfileColorClick(workingProfile, colorType);
    }
}