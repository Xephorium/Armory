package com.xephorium.armory.repository;

import com.xephorium.armory.model.Faction;
import com.xephorium.armory.model.Profile;
import com.xephorium.armory.model.ProfileList;
import com.xephorium.armory.model.converter.ProfileConverter;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class GameRepository {


    /*--- Variables ---*/

    private static final String PLAYER_COLORS_FILE_NAME = "playercolors.xml";
    private static final String PLAYER_COLORS_PATH = "overrides\\data";
    private static final String INSTALLATION_FILE_NAME = "InstallationDirectory.txt";
    private static final File INSTALLATION_FILE = new File(getCurrentDirectory() + "\\" + INSTALLATION_FILE_NAME);

    private static final Integer UNSC_PROFILE_START_INDEX = 3;
    private static final Integer UNSC_PROFILE_END_INDEX = 9;
    private static final Integer COVENANT_PROFILE_START_INDEX = 9;
    private static final Integer COVENANT_PROFILE_END_INDEX = 15;


    /*--- Public Methods ---*/

    public ProfileList mergeStartupProfileListWithCurrentGameConfiguration(ProfileList profileList) {
        ProfileList startupProfileList = profileList.clone();
        ProfileList playerColorsProfileList = new ProfileList();
        ProfileList mergedProfileList = startupProfileList.clone();

        playerColorsProfileList.addAllNewProfilesAsIs(loadSavedFactionPlayerColorsList(Faction.UNSC));
        playerColorsProfileList.addAllNewProfilesAsIs(loadSavedFactionPlayerColorsList(Faction.COVENANT));
        ProfileList playerColorsDifferenceList = playerColorsProfileList.clone();

        // Merge startupProfileList and playerColorsProfileList
        for (int y = 0; y < playerColorsProfileList.size(); y++) {
            Profile currentPlayerColorsProfile = playerColorsProfileList.getProfileByIndex(y).cloneProfile();

            if (startupProfileList.containsProfileByNameAndColors(currentPlayerColorsProfile)
                    || currentPlayerColorsProfile.getPrimaryKey() < 0) {
                playerColorsDifferenceList.deleteByNameAndColors(currentPlayerColorsProfile);
            }
        }

        if (playerColorsDifferenceList.size() > 0) {
            mergedProfileList.addAllNewProfiles(playerColorsDifferenceList);
        }
        mergedProfileList = mergedProfileList.sortProfileListByPrimaryKey(mergedProfileList);

        return mergedProfileList;
    }

    public boolean saveFactionProfiles(Faction faction,
                                       List<Integer> profileConfiguration,
                                       ProfileList profileList) {

        // Read Current PlayerColors Contents
        List<String> playerColorsContents = readPlayerColorsContents();
        if (playerColorsContents == null) {
            return false;
        }

        // Create Faction-Specific Profile List
        ProfileList factionProfiles = new ProfileList();
        for (Integer primaryKey : profileConfiguration) {
            factionProfiles.addNewProfile(profileList.getProfileByPrimaryKey(primaryKey).cloneProfile());
        }

        // Replace Appropriate Lines of PlayerColors
        for (int x = getFactionStartIndex(faction); x < getFactionEndIndex(faction); x++) {
            int profileIndex = x - getFactionStartIndex(faction);
            int colorNumber = x - 3;
            String saveString = ProfileConverter
                    .getSaveStringFromProfile(factionProfiles.getProfileByIndex(profileIndex), colorNumber + 1);
            playerColorsContents.set(x, saveString);
        }

        // Write to PlayerColors
        try {
            PrintWriter writer = new PrintWriter(getGamePlayerColorsFile(), "UTF-8");
            playerColorsContents.forEach(writer::println);
            writer.close();
        } catch (IOException exception) {
            return false;
        }

        return true;
    }

    public static boolean isValidHaloWarsInstallation(String directory) {
        File installationDirectory = new File(directory);
        boolean launcherFound = false;
        boolean creviceFound = false;

        if (installationDirectory.exists()) {
            for (File file : installationDirectory.listFiles()) {

                if (file.getName().equals("xgameFinal.exe")) {
                    launcherFound = true;
                }

                if (file.getName().equals("crevice.era")) {
                    creviceFound = true;
                }
            }
        } else {
            return false;
        }

        return launcherFound && creviceFound;
    }

    public boolean isInstallationDirectorySet() {
        return loadInstallationDirectory() != null;
    }

    public String loadInstallationDirectory() {
        String installationDirectory = null;

        if (INSTALLATION_FILE.exists()) {
            String directory = readInstallationDirectoryFromSaveFile();
            if (directory != null && isValidHaloWarsInstallation(directory)) {
                installationDirectory = directory;
            }
        } else {
            return null;
        }

        return installationDirectory;
    }

    public boolean saveInstallationDirectory(String directory) {
        if (INSTALLATION_FILE.exists()) {
            return writeInstallationDirectoryToSaveFile(directory);
        } else {
            return createInstallationDirectorySaveFile() && writeInstallationDirectoryToSaveFile(directory);
        }
    }

    public static boolean isUserLocalDirectoryFound() {
        String user = SystemRepository.getUsername();
        File userLocalDirectory = new File("C:\\Users\\" + user + "\\AppData\\Local");
        return userLocalDirectory.exists();
    }

    public void setupModManifestFile(String directory) {
        String user = SystemRepository.getUsername();
        File modManifestFolder = new File("C:\\Users\\" + user + "\\AppData\\Local\\Halo\u00A0Wars");
        File modManifestFile = new File("C:\\Users\\" + user + "\\AppData\\Local\\Halo\u00A0Wars\\ModManifest.txt");

        try {
            modManifestFolder.mkdir();
            if (!modManifestFile.exists()) try {
                modManifestFile.createNewFile();
                PrintWriter writer = new PrintWriter(modManifestFile, "UTF-8");
                writer.println(directory + "\\overrides");
                writer.close();
            } catch (Exception exception) {
                // Do Nothing
            }
        } catch (Exception exception) {
            // No Nothing
        }
    }

    public void setupPlayerColorsFile() {

        // Initialize Directory
        File playerColorsDirectory = new File(loadInstallationDirectory() + "\\" + PLAYER_COLORS_PATH);
        if (!playerColorsDirectory.exists()) {
            try {
                playerColorsDirectory.mkdirs();
            } catch (Exception exception) { /* Do Nothing */ }
        }

        // Initialize File
        File playerColorsFile = getGamePlayerColorsFile();
        if (!playerColorsFile.exists()) {
            try {
                playerColorsFile.createNewFile();
                List<String> defaultPlayerColorsContents = getDefaultPlayerColorsContents();
                PrintWriter writer = new PrintWriter(playerColorsFile, "UTF-8");
                defaultPlayerColorsContents.forEach(string -> writer.println(string));
                writer.close();
            } catch (Exception exception) { /* Do Nothing */ }
        }
    }

    public List<Integer> loadSavedFactionConfiguration(Faction faction, ProfileList profileList) {
        ProfileList savedFactionPlayerColorsList = loadSavedFactionPlayerColorsList(faction);
        if (savedFactionPlayerColorsList == null) {
            return null;
        }

        List<Integer> savedFactionConfiguration = new ArrayList<>(6);
        for (int x = 0; x < savedFactionPlayerColorsList.size(); x++) {
            Profile savedFactionPlayerColorProfile = savedFactionPlayerColorsList.getProfileByIndex(x);

            for (int y = 0; y < profileList.size(); y++) {
                Profile workingProfile = profileList.getProfileByIndex(y);
                if (savedFactionPlayerColorProfile.equalsColors(workingProfile)
                        && savedFactionPlayerColorProfile.getName().equals(workingProfile.getName())) {
                    savedFactionConfiguration.add(workingProfile.getPrimaryKey());
                    break;
                }
            }
        }

        return savedFactionConfiguration;
    }

    public List<Integer> getDefaultUNSCPlayerConfiguration() {
        List<Integer> unscDefaultProfileKeys = new ArrayList<>();
        ProfileList unscDefaultProfileList = getDefaultUNSCPlayerProfiles();
        for (int x = 0; x < unscDefaultProfileList.size(); x++) {
            unscDefaultProfileKeys.add(unscDefaultProfileList.getProfileByIndex(x).getPrimaryKey());
        }
        return unscDefaultProfileKeys;
    }

    public List<Integer> getDefaultCovenantPlayerConfiguration() {
        List<Integer> covenantDefaultProfileKeys = new ArrayList<>();
        ProfileList covenantDefaultProfileList = getDefaultCovenantPlayerProfiles();
        for (int x = 0; x < covenantDefaultProfileList.size(); x++) {
            covenantDefaultProfileKeys.add(covenantDefaultProfileList.getProfileByIndex(x).getPrimaryKey());
        }
        return covenantDefaultProfileKeys;
    }

    /*
      Note: UNSC/Covenant profiles must be hardcoded with consistently
      absurd, negative primary keys. This is to prevent custom profile
      upon override upon Configuration reset.
    */
    public ProfileList getDefaultUNSCPlayerProfiles() {
        ProfileList profileList = new ProfileList();
        profileList.addNewProfile(new Profile(-101, "UNSC - Olive", new Color(145, 163, 85), new Color(35, 55, 12), new Color(69, 110, 24), new Color(69, 110, 24), new Color(69, 110, 24)));
        profileList.addNewProfile(new Profile(-102, "UNSC - Orange", new Color(235, 165, 27), new Color(117, 82, 14), new Color(235, 165, 27), new Color(235, 165, 27), new Color(235, 165, 27)));
        profileList.addNewProfile(new Profile(-103, "UNSC - Navy Blue", new Color(82, 125, 179), new Color(33, 55, 75), new Color(65, 110, 150), new Color(65, 110, 150), new Color(65, 110, 150)));
        profileList.addNewProfile(new Profile(-104, "UNSC - Tomato Red", new Color(215, 99, 68), new Color(100, 30, 0), new Color(200, 60, 0), new Color(200, 60, 0), new Color(200, 60, 0)));
        profileList.addNewProfile(new Profile(-105, "UNSC - Gunmetal Gray", new Color(83, 83, 83), new Color(25, 25, 15), new Color(50, 50, 30), new Color(50, 50, 30), new Color(50, 50, 30)));
        profileList.addNewProfile(new Profile(-106, "UNSC - Desert Brown", new Color(185, 160, 125), new Color(93, 50, 63), new Color(185, 160, 125), new Color(185, 160, 125), new Color(185, 160, 125)));
        return profileList;
    }

    /*
      Note: UNSC/Covenant profiles must be hardcoded with consistently
      absurd, negative primary keys. This is to prevent custom profile
      upon override upon Configuration reset.
    */
    public ProfileList getDefaultCovenantPlayerProfiles() {
        ProfileList profileList = new ProfileList();
        profileList.addNewProfile(new Profile(-107, "Covenant - Violet", new Color(165, 95, 245), new Color(83, 48, 172), new Color(165, 95, 245), new Color(165, 95, 245), new Color(165, 95, 245)));
        profileList.addNewProfile(new Profile(-108, "Covenant - Teal", new Color(0, 175, 200), new Color(0, 83, 100), new Color(0, 175, 200), new Color(0, 175, 200), new Color(0, 175, 200)));
        profileList.addNewProfile(new Profile(-109, "Covenant - Blood Red", new Color(210, 20, 0), new Color(105, 10, 0), new Color(210, 20, 0), new Color(210, 20, 0), new Color(210, 20, 0)));
        profileList.addNewProfile(new Profile(-110, "Covenant - Forest Green", new Color(40, 50, 20), new Color(20, 25, 10), new Color(40, 50, 20), new Color(40, 50, 20), new Color(40, 50, 20)));
        profileList.addNewProfile(new Profile(-111, "Covenant - Yellow", new Color(255, 215, 50), new Color(128, 107, 25), new Color(255, 215, 50), new Color(255, 215, 50), new Color(255, 215, 50)));
        profileList.addNewProfile(new Profile(-112, "Covenant - Beige", new Color(215, 190, 175), new Color(108, 95, 87), new Color(215, 200, 175), new Color(215, 200, 175), new Color(215, 200, 175)));
        return profileList;
    }

    public boolean isDefaultProfilePrimaryKey(int primaryKey) {
        return primaryKey < -100 && primaryKey > -113;
    }


    /*--- Private Methods ---*/

    private Integer getFactionStartIndex(Faction faction) {
        if (faction == Faction.UNSC) {
            return UNSC_PROFILE_START_INDEX;
        } else {
            return COVENANT_PROFILE_START_INDEX;
        }
    }

    private Integer getFactionEndIndex(Faction faction) {
        if (faction == Faction.UNSC) {
            return UNSC_PROFILE_END_INDEX;
        } else {
            return COVENANT_PROFILE_END_INDEX;
        }
    }

    private ProfileList loadSavedFactionPlayerColorsList(Faction faction) {
        List<String> playerColorsContents = readPlayerColorsContents();
        if (playerColorsContents == null) {
            return null;
        }

        ProfileList savedFactionPlayerColorsList = new ProfileList();
        for (int x = getFactionStartIndex(faction); x < getFactionEndIndex(faction); x++) {
            Profile currentProfile = ProfileConverter.getProfileFromSaveString(playerColorsContents.get(x));
            if (currentProfile != null) {
                savedFactionPlayerColorsList.addNewProfileAsIs(currentProfile);
            }
        }
        return savedFactionPlayerColorsList;
    }

    private List<String> readPlayerColorsContents() {
        File playerColorsFile = getGamePlayerColorsFile();
        if (playerColorsFile == null) {
            setupPlayerColorsFile();
            playerColorsFile = getGamePlayerColorsFile();
        }

        List<String> playerColorsContents = new ArrayList<>();
        BufferedReader bufferedReader;

        try {
            bufferedReader = new BufferedReader(new FileReader(playerColorsFile));
            String line = bufferedReader.readLine();

            while (line != null) {
                playerColorsContents.add(line);
                line = bufferedReader.readLine();
            }

            bufferedReader.close();
        } catch (IOException exception) {
            return null;
        }

        return playerColorsContents;
    }

    private List<String> getDefaultPlayerColorsContents() {
        File defaultPlayerColorsFile = new File("reference\\files\\playercolors.xml");
        List<String> defaultPlayerColorsContents = new ArrayList<String>();
        BufferedReader bufferedReader;

        try {
            bufferedReader = new BufferedReader(new FileReader(defaultPlayerColorsFile));
            String line = bufferedReader.readLine();

            while (line != null) {
                defaultPlayerColorsContents.add(line);
                line = bufferedReader.readLine();
            }

            bufferedReader.close();
        } catch (IOException exception) {
            System.out.println("Exception - " + exception);
        }

        return defaultPlayerColorsContents;
    }

    private File getGamePlayerColorsFile() {
        return new File(loadInstallationDirectory() + "\\" + PLAYER_COLORS_PATH + "\\" + PLAYER_COLORS_FILE_NAME);
    }

    private boolean isSavedInstallationDirectoryValid() {
        String savedDirectory = loadInstallationDirectory();
        return loadInstallationDirectory() != null && isValidHaloWarsInstallation(savedDirectory);
    }

    private static boolean createInstallationDirectorySaveFile() {
        try {
            INSTALLATION_FILE.createNewFile();
        } catch (IOException exception) {
            return false;
        }
        return true;
    }

    private static String readInstallationDirectoryFromSaveFile() {
        String installationDirectory = null;
        BufferedReader bufferedReader;

        try {
            bufferedReader = new BufferedReader(new FileReader(INSTALLATION_FILE));
            String directory = bufferedReader.readLine();

            if (directory != null && directory.trim().length() > 0) {
                installationDirectory = directory;
            }

            bufferedReader.close();
        } catch (IOException exception) {
            // Do Nothing
        }

        return installationDirectory;
    }

    private static boolean writeInstallationDirectoryToSaveFile(String directory) {
        try {
            PrintWriter writer = new PrintWriter(INSTALLATION_FILE, "UTF-8");
            writer.println(directory);
            writer.close();
        } catch (IOException exception) {
            return false;
        }

        return true;
    }

    private static String getCurrentDirectory() {
        return System.getProperty("user.dir");
    }

}
