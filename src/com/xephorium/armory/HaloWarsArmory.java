package com.xephorium.armory;

import com.xephorium.armory.model.Profile;
import com.xephorium.armory.repository.MockProfileRepository;
import com.xephorium.armory.ui.ArmoryWindow;

public class HaloWarsArmory implements ArmoryWindow.ArmoryWindowListener {


    /*--- Variables ---*/

    private ArmoryWindow armoryWindow;
    Profile[] mockProfileList;


    /*--- Constructor ---*/

    public HaloWarsArmory() {
        armoryWindow = new ArmoryWindow(this);
        armoryWindow.displayWindow();

        mockProfileList = MockProfileRepository.getProfileList();
        armoryWindow.updateProfileList(mockProfileList);
    }


    /*--- Armory Window Overrides ---*/

    @Override
    public void handleBrowseButtonClick() {
        armoryWindow.displayDirectoryChooser();
    }

    @Override
    public void handleDirectorySelection(String directory) {
        if (true /* Valid Halo Wars Installation */) {
            armoryWindow.setValidInstallDirectory(directory);
            armoryWindow.displayGameFoundDialog();
        } else {
            armoryWindow.setInvalidInstallDirectory();
            armoryWindow.displayGameNotFoundDialog();
        }
    }

    @Override
    public void handleWorkingProfileSaveClick(Profile newProfile) {
        // TODO - Write Changes to File
        mockProfileList = Profile.getUpdatedProfileList(mockProfileList, newProfile);
        armoryWindow.updateProfileList(mockProfileList);
    }

    @Override
    public void handleProfileSelection(Profile profile) {
        armoryWindow.setSelectedProfile(profile);
    }
}