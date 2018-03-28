package com.xephorium.armory;

import com.xephorium.armory.model.Profile;
import com.xephorium.armory.repository.MockProfileRepository;
import com.xephorium.armory.ui.ArmoryWindow;
import com.xephorium.armory.ui.utility.ColorChooser.ColorChooserListener;
import com.xephorium.armory.ui.utility.StringUtility;

import java.awt.*;
import java.util.Arrays;

public class HaloWarsArmory implements ArmoryWindow.ArmoryWindowListener {


    /*--- Variables ---*/

    private ArmoryWindow armoryWindow;
    private Profile[] profileList;


    /*--- Constructor ---*/

    public HaloWarsArmory() {
        armoryWindow = new ArmoryWindow(this);
        armoryWindow.displayWindow();

        profileList = MockProfileRepository.getProfileList();
        armoryWindow.updateProfileList(getProfileList());
    }


    /*--- Private Methods ---*/

    private Profile[] getProfileList() {
        return Profile.cloneProfileList(profileList);
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
        if (StringUtility.isBlank(newProfile.getName())) {
            armoryWindow.displayProfileMustHaveNameDialog();
            return;
        }

        this.profileList = Profile.getUpdatedProfileList(getProfileList(), newProfile);
        armoryWindow.updateProfileList(getProfileList());
        armoryWindow.displayProfileSavedDialog();
        // TODO - Write Changes to File
    }

    @Override
    public void handleProfileSelection(Profile profile) {
        armoryWindow.setSelectedProfile(profile);
    }

    @Override
    public void handleWorkingProfileColorClick(Profile workingProfile, Profile.ColorType colorType) {
        armoryWindow.displayColorChooserDialog(workingProfile.getColor(colorType), new ColorChooserListener() {
            @Override
            public void onColorSelection(Color color) {
                workingProfile.setColor(colorType, color);
                armoryWindow.setWorkingProfile(workingProfile);
                // TODO - Write Changes to File
            }

            @Override
            public void onDialogClose() {
                // Do Nothing
            }
        });
    }
}