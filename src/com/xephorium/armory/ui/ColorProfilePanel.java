package com.xephorium.armory.ui;

import com.xephorium.armory.model.Profile;
import com.xephorium.armory.model.Profile.ColorType;
import com.xephorium.armory.ui.resource.color.ArmoryColor;
import com.xephorium.armory.ui.resource.dimension.ArmoryDimension;
import com.xephorium.armory.ui.resource.font.ArmoryFont;
import com.xephorium.armory.ui.utility.CustomMouseListener;
import com.xephorium.armory.ui.utility.CustomMouseListener.MouseClickListener;
import com.xephorium.armory.ui.utility.StringUtility;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ColorProfilePanel extends JPanel {


    /*--- Variables ---*/

    private ColorProfilePanelListener listener;
    private JTextField profileNameTextField;
    private JPanel[] profileColorPanels = new JPanel[ColorType.values().length];
    private JList profileListPanel;

    private Profile[] profiles;

    /*--- Constructor ---*/

    public ColorProfilePanel(ColorProfilePanelListener listener) {
        this.listener = listener;

        initializePanelAttributes();

        this.add(createProfileEditPanel());
        this.add(createProfilePreviewPanel());
    }


    /*--- Public Methods ---*/

    public String getWorkingProfileName() {
        return StringUtility.removeLeadingSpace(this.profileNameTextField.getText());
    }

    public void setWorkingProfileName(String name) {
        this.profileNameTextField.setText(name);
    }

    public Color getWorkingProfileColor(ColorType colorType) {
        return profileColorPanels[colorType.getIndex()].getBackground();
    }

    public void setWorkingProfileColor(ColorType colorType, Color color) {
        profileColorPanels[colorType.getIndex()].setBackground(color);
    }

    public Color[] getWorkingProfileColors() {
        Color[] colors = new Color[profileColorPanels.length];
        for (int x = 0; x < profileColorPanels.length; x++) {
            colors[x] = profileColorPanels[x].getBackground();
        }
        return colors;
    }

    public void setWorkingProfileColors(Color[] colors) {
        for (int x = 0; x < profileColorPanels.length; x++) {
            profileColorPanels[x].setBackground(colors[x]);
        }
    }

    public Profile getWorkingProfile() {
        Profile workingProfile = new Profile();
        workingProfile.setName(getWorkingProfileName());
        workingProfile.setColors(getWorkingProfileColors());
        return workingProfile;
    }

    public void setWorkingProfile(Profile profile) {
        setWorkingProfileName(profile.getName());
        setWorkingProfileColors(profile.getColors());
    }

    public void setProfiles(Profile[] profiles) {
        this.profiles = profiles;
        updateProfileBrowsePanel();
    }

    public Profile[] getProfiles() {
        return this.profiles;
    }


    /*--- Private Methods ---*/

    private void initializePanelAttributes() {
        this.setLayout(new GridLayout(2, 1, 0, ArmoryDimension.PANEL_PADDING));
        this.setBorder(new EmptyBorder(
                ArmoryDimension.PANEL_PADDING/2,
                ArmoryDimension.PANEL_PADDING/2,
                ArmoryDimension.WINDOW_PADDING_VERTICAL,
                ArmoryDimension.WINDOW_PADDING_HORIZONTAL));
        this.setPreferredSize(new Dimension(ArmoryDimension.COLOR_PROFILE_PANEL_WIDTH, 0));
        this.setBackground(ArmoryColor.WINDOW_BACKGROUND_COLOR);
    }

    private JPanel createProfileEditPanel() {

        JPanel profileEditBorderedPanel = new JPanel(new BorderLayout());
        JPanel profileEditPanel = new JPanel(new BorderLayout());

        profileEditBorderedPanel.setBorder(BorderFactory.createLineBorder(ArmoryColor.WINDOW_BORDER_COLOR_LIGHT));
        profileEditPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        profileEditPanel.setBackground(Color.WHITE);

        profileEditPanel.add(createProfileBrowsePanel(), BorderLayout.CENTER);
        profileEditPanel.add(createProfileSettingsPanel(), BorderLayout.LINE_END);
        profileEditBorderedPanel.add(profileEditPanel, BorderLayout.CENTER);

        return profileEditBorderedPanel;
    }

    private JPanel createProfileBrowsePanel() {

        JPanel profileBrowsePanel = new JPanel(new BorderLayout());
        profileBrowsePanel.setBackground(Color.WHITE);

        JLabel profileListHeaderLabel = new JLabel("Color Profiles");
        profileListHeaderLabel.setFont(ArmoryFont.LARGE);
        profileListHeaderLabel.setBorder(new EmptyBorder(0,0,5,5));

        profileListPanel = new JList();
        profileListPanel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        profileListPanel.setLayoutOrientation(JList.VERTICAL);
        profileListPanel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    selectProfileByIndex(profileListPanel.getLeadSelectionIndex());
                }
            }
        });
        JScrollPane profileListScroller = new JScrollPane(profileListPanel);

        profileBrowsePanel.add(profileListHeaderLabel, BorderLayout.PAGE_START);
        profileBrowsePanel.add(profileListScroller, BorderLayout.CENTER);

        return profileBrowsePanel;
    }

    private void updateProfileBrowsePanel() {
        String[] profileNames = new String[profiles.length];
        for (int x = 0; x < profiles.length; x++) {
            profileNames[x] = profiles[x].getName();
        }
        profileListPanel.setListData(profileNames);
        profileListPanel.setSelectedIndex(0);
    }

    private JPanel createProfileSettingsPanel() {

        JPanel profileSettingsPanel = new JPanel(new BorderLayout());
        profileSettingsPanel.setPreferredSize(new Dimension(135, 0));
        profileSettingsPanel.setBorder(new EmptyBorder(0,15,0,0));
        profileSettingsPanel.setBackground(Color.WHITE);

        profileNameTextField = new JTextField();
        profileNameTextField.setBackground(Color.WHITE);
        setWorkingProfileName("Profile Name");

        JPanel profileColorsPanel = new JPanel(new GridLayout(ColorType.values().length, 1));
        profileColorsPanel.setBorder(new EmptyBorder(5,0,5,0));
        profileColorsPanel.setBackground(Color.WHITE);
        for (ColorType colorType :ColorType.values()) {
            profileColorsPanel.add(createColorListItem(colorType));
        }

        JButton profileSaveButton = new JButton("Save");
        profileSaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.handleProfileSaveClick();
            }
        });

        profileSettingsPanel.add(profileNameTextField, BorderLayout.PAGE_START);
        profileSettingsPanel.add(profileColorsPanel, BorderLayout.CENTER);
        profileSettingsPanel.add(profileSaveButton, BorderLayout.PAGE_END);

        return profileSettingsPanel;
    }

    private JPanel createColorListItem(ColorType colorType) {

        JPanel colorListItem = new JPanel(new BorderLayout());
        colorListItem.addMouseListener(createColorListItemMouseListener(colorType));
        colorListItem.setBackground(Color.WHITE);

        JPanel colorPreviewPanel = new JPanel(new GridBagLayout());
        colorPreviewPanel.setBackground(Color.WHITE);
        profileColorPanels[colorType.getIndex()] = new JPanel();
        profileColorPanels[colorType.getIndex()].setBorder(BorderFactory.createLineBorder(ArmoryColor.WINDOW_BORDER_COLOR_DARK));
        profileColorPanels[colorType.getIndex()].setBackground(ArmoryColor.WINDOW_TEST_COLOR);
        profileColorPanels[colorType.getIndex()].setPreferredSize(new Dimension(15, 15));
        colorPreviewPanel.add(profileColorPanels[colorType.getIndex()], new GridBagConstraints());

        colorListItem.add(new JLabel(colorType.getDisplayName()), BorderLayout.CENTER);
        colorListItem.add(colorPreviewPanel, BorderLayout.LINE_END);

        return colorListItem;
    }

    private CustomMouseListener createColorListItemMouseListener(ColorType colorType) {
        return new CustomMouseListener(new MouseClickListener() {
            @Override
            public void onMouseClick() {
                listener.handleProfileColorClick(colorType, getWorkingProfileColor(colorType));
            }
        });
    }

    private JPanel createProfilePreviewPanel() {
        JPanel profilePreviewPanel = new JPanel();
        profilePreviewPanel.setBackground(ArmoryColor.WINDOW_TEST_COLOR);
        profilePreviewPanel.setBorder(BorderFactory.createLineBorder(ArmoryColor.WINDOW_BORDER_COLOR_LIGHT));
        return profilePreviewPanel;
    }

    private void selectProfileByIndex(int index) {
        setWorkingProfileName(profiles[index].getName());
        setWorkingProfileColors(profiles[index].getColors());
    }


    /*--- Listener Interface ---*/

    interface ColorProfilePanelListener {

        void handleProfileColorClick(ColorType colorType, Color currentColor);

        void handleProfileSaveClick();
    }
}
