package com.xephorium.armory;

import com.xephorium.armory.model.Profile;
import com.xephorium.armory.model.ProfileList;
import com.xephorium.armory.repository.MockProfileRepository;
import com.xephorium.armory.ui.ArmoryWindow;
import com.xephorium.armory.ui.ArmoryWindowListener;
import com.xephorium.armory.ui.utility.ColorChooser.ColorChooserListener;
import com.xephorium.armory.ui.utility.StringUtility;

import java.awt.*;
import java.util.List;

public class HaloWarsArmory implements ArmoryWindowListener {


    /*--- Variables ---*/

    private ArmoryWindow armoryWindow;
    private ProfileList profileList;
    private List<Integer> unscPlayerConfiguration;
    private List<Integer> covenantPlayerConfiguration;


    /*--- Constructor ---*/

    public HaloWarsArmory() {
        armoryWindow = new ArmoryWindow(this);
        armoryWindow.displayWindow();

        profileList = MockProfileRepository.getProfileList();
        unscPlayerConfiguration = MockProfileRepository.loadCustomPlayerConfiguration();
        covenantPlayerConfiguration = MockProfileRepository.loadCustomPlayerConfiguration();

        armoryWindow.updateProfileList(profileList);
        armoryWindow.updateUNSCPlayerConfiguration(unscPlayerConfiguration);
        armoryWindow.updateCovenantPlayerConfiguration(covenantPlayerConfiguration);
    }


    /*--- Armory Window Overrides ---*/


    // Installation Directory

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


    // Faction Configuration

    @Override
    public void handleUNSCPlayerUpdate(int playerNumber, int profilePrimaryKey) {
        unscPlayerConfiguration.set(playerNumber, profilePrimaryKey);
    }

    @Override
    public void handleCovenantPlayerUpdate(int playerNumber, int profilePrimaryKey) {
        covenantPlayerConfiguration.set(playerNumber, profilePrimaryKey);
    }

    @Override
    public void handleUNSCConfigurationReset() {
        unscPlayerConfiguration = MockProfileRepository.loadDefaultUNSCPlayerConfiguration();
        armoryWindow.updateUNSCPlayerConfiguration(unscPlayerConfiguration);
    }

    @Override
    public void handleCovenantConfigurationReset() {
        covenantPlayerConfiguration = MockProfileRepository.loadDefaultCovenantPlayerConfiguration();
        armoryWindow.updateCovenantPlayerConfiguration(covenantPlayerConfiguration);
    }

    @Override
    public void handleUNSCConfigurationSave() {
        // TODO - Write Changes to File
    }

    @Override
    public void handleCovenantConfigurationSave() {
        // TODO - Write Changes to File
    }


    // Profile Configuration

    @Override
    public void handleWorkingProfileSaveClick(Profile newProfile) {
        if (StringUtility.isBlank(newProfile.getName())) {
            armoryWindow.displayProfileMustHaveNameDialog();
            return;
        }

        if (newProfile.equals(profileList.getByPrimaryKey(newProfile.getPrimaryKey()))) {
            armoryWindow.displayNoChangesToSaveDialog();
            return;
        }

        profileList.add(newProfile);
        armoryWindow.updateProfileList(profileList);
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