package com.xephorium.armory.repository;

import com.xephorium.armory.model.Profile;
import com.xephorium.armory.model.converter.ProfileConverter;
import com.xephorium.armory.model.ProfileList;

import java.io.*;

public class CustomProfileRepository {


    /*--- Variables ---*/

    private static final String CUSTOM_PROFILES_FILE_NAME = "CustomProfiles.txt";
    private static final File CUSTOM_PROFILES_FILE = new File(getCurrentDirectory() + "\\" + CUSTOM_PROFILES_FILE_NAME);


    /*--- Public Methods ---*/

    public ProfileList loadCustomPlayerProfileList() {
        ProfileList profileList;

        if (CUSTOM_PROFILES_FILE.exists()) {
            profileList = createNewProfileListFromCustomProfiles();
            writeProfileListToCustomProfiles(profileList);
        } else {
            return new ProfileList();
        }

        return profileList;
    }

    public boolean saveCustomPlayerProfile(Profile profile) {
        if (CUSTOM_PROFILES_FILE.exists()) {
            return writeProfileToCustomProfiles(profile);
        } else {
            return createCustomProfilesSaveFile() && writeProfileToCustomProfiles(profile);
        }
    }

    public boolean saveCustomPlayerProfileList(ProfileList profileList) {
        boolean saveSuccessful = true;
        if (CUSTOM_PROFILES_FILE.exists()) {
            for (int x = 0; x < profileList.size(); x++) {
                if(!writeProfileToCustomProfiles(profileList.getProfileByIndex(x)))
                    saveSuccessful = false;
            }
        } else {
            if(!createCustomProfilesSaveFile())
                return false;
            for (int x = 0; x < profileList.size(); x++) {
                if(!writeProfileToCustomProfiles(profileList.getProfileByIndex(x)))
                    saveSuccessful = false;
            }
        }

        return saveSuccessful;
    }

    public void deleteCustomPlayerProfile(Profile profile) {
        if (CUSTOM_PROFILES_FILE.exists()) {
            deleteProfileFromCustomProfilesByPrimaryKey(profile);
        }
    }


    /*--- Private Methods ---*/

    private static boolean writeProfileToCustomProfiles(Profile profile) {
        ProfileList customProfileList = readCustomProfiles();

        if (customProfileList.containsPrimaryKey(profile.getPrimaryKey())) {
            customProfileList.updateExistingProfile(profile);
        } else {
            customProfileList.addNewProfile(profile);
        }

        return writeProfileListToCustomProfiles(customProfileList);
    }

    private static boolean writeProfileListToCustomProfiles(ProfileList profileList) {
        try {
            PrintWriter writer = new PrintWriter(CUSTOM_PROFILES_FILE, "UTF-8");
            for (int x = 0; x < profileList.size(); x++) {
                writer.println(ProfileConverter.getSaveStringFromProfile(profileList.getProfileByIndex(x), null));
            }
            writer.close();
        } catch (IOException exception) {
            return false;
        }

        return true;
    }

    private static boolean deleteProfileFromCustomProfilesByPrimaryKey(Profile profile) {
        ProfileList customProfileList = readCustomProfiles();

        if (customProfileList.containsPrimaryKey(profile.getPrimaryKey())) {
            customProfileList.deleteByPrimaryKey(profile.getPrimaryKey());
        }

        return writeProfileListToCustomProfiles(customProfileList);
    }

    private static boolean deleteProfileFromCustomProfilesByNameAndColors(Profile profile) {
        ProfileList customProfileList = readCustomProfiles();

        if (customProfileList.containsProfileByNameAndColors(profile)) {
            customProfileList.deleteByNameAndColors(profile);
        }

        return writeProfileListToCustomProfiles(customProfileList);
    }

    private static ProfileList readCustomProfiles() {
        ProfileList customProfileList = new ProfileList();
        BufferedReader bufferedReader;

        try {
            bufferedReader = new BufferedReader(new FileReader(CUSTOM_PROFILES_FILE));
            String line = bufferedReader.readLine();

            while (line != null) {
                Profile profile = ProfileConverter.getProfileFromSaveString(line);
                if (profile != null)
                    customProfileList.addNewProfileAsIs(profile);
                line = bufferedReader.readLine();
            }

            bufferedReader.close();
        } catch (IOException exception) {
            return new ProfileList();
        }

        return customProfileList;
    }

    private static ProfileList createNewProfileListFromCustomProfiles() {
        ProfileList customProfileList = new ProfileList();
        BufferedReader bufferedReader;

        try {
            bufferedReader = new BufferedReader(new FileReader(CUSTOM_PROFILES_FILE));
            String line = bufferedReader.readLine();

            while (line != null) {
                Profile profile = ProfileConverter.getProfileFromSaveString(line);
                if (profile != null)
                    customProfileList.addNewProfile(profile);
                line = bufferedReader.readLine();
            }

            bufferedReader.close();
        } catch (IOException exception) {
            return new ProfileList();
        }

        return customProfileList;
    }

    private static boolean createCustomProfilesSaveFile() {
        try {
            CUSTOM_PROFILES_FILE.createNewFile();
        } catch (IOException exception) {
            return false;
        }
        return true;
    }

    private static String getCurrentDirectory() {
        return System.getProperty("user.dir");
    }
}
