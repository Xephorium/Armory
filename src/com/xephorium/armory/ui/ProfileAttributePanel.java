package com.xephorium.armory.ui;

import com.xephorium.armory.model.Profile;
import com.xephorium.armory.ui.resource.color.ArmoryColor;
import com.xephorium.armory.ui.utility.CustomMouseListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfileAttributePanel extends JPanel {


    /*--- Variables ---*/

    private ProfileAttributePanelListener listener;
    private JTextField profileNameTextField;
    private JPanel[] profileColorPanels = new JPanel[Profile.ColorType.values().length];
    private Profile workingPrimaryKey;


    /*--- Constructor ---*/

    public ProfileAttributePanel(ProfileAttributePanelListener listener) {
        super();
        this.listener = listener;

        initializePanelAttributes();
        initializeProfileNameTextField();

        this.add(profileNameTextField, BorderLayout.PAGE_START);
        this.add(createColorListPanel(), BorderLayout.CENTER);
        this.add(createSaveButton(), BorderLayout.PAGE_END);
    }


    /*--- Public Methods ---*/

    public void setWorkingProfile(Profile profile) {
        workingPrimaryKey = profile;
        setWorkingProfileName(workingPrimaryKey.getName());
        setWorkingProfileColors(workingPrimaryKey.getColors());
    }

    public void setWorkingProfileName(String name) {
        workingPrimaryKey.setName(name);
        profileNameTextField.setText(name);
    }

    public void setWorkingProfileColor(Profile.ColorType colorType, Color color) {
        workingPrimaryKey.setColor(colorType, color);
        profileColorPanels[colorType.getIndex()].setBackground(color);
    }

    public void setWorkingProfileColors(Color[] colors) {
        workingPrimaryKey.setColors(colors);
        for (int x = 0; x < profileColorPanels.length; x++) {
            profileColorPanels[x].setBackground(colors[x]);
        }
    }


    /*--- Private Getter Methods ---*/

    private Profile getWorkingProfile() {
        return workingPrimaryKey;
    }

    private String getWorkingProfileName() {
        return workingPrimaryKey.getName();
    }

    private Color[] getWorkingProfileColors() {
        return workingPrimaryKey.getColors();
    }


    /*--- Private Setup Methods ---*/

    private void initializePanelAttributes() {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(135, 0));
        this.setBorder(new EmptyBorder(0,15,0,0));
        this.setBackground(Color.WHITE);
    }

    private void initializeProfileNameTextField() {
        profileNameTextField = new JTextField();
        profileNameTextField.setBackground(Color.WHITE);
        profileNameTextField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ArmoryColor.WINDOW_BORDER_COLOR_DARK),
                new EmptyBorder(3,2,3,2)));
        profileNameTextField.setText("Profile Name");
    }

    private JPanel createColorListPanel() {
        JPanel colorListPanel = new JPanel(new GridLayout(Profile.ColorType.values().length, 1));
        colorListPanel.setBorder(new EmptyBorder(5,0,5,0));
        colorListPanel.setBackground(Color.WHITE);
        for (Profile.ColorType colorType : Profile.ColorType.values()) {
            colorListPanel.add(createColorListItem(colorType));
        }
        return colorListPanel;
    }

    private JPanel createColorListItem(Profile.ColorType colorType) {
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

    private JButton createSaveButton() {
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.handleWorkingProfileSaveClick(getWorkingProfile());
            }
        });
        return saveButton;
    }

    private CustomMouseListener createColorListItemMouseListener(Profile.ColorType colorType) {
        return new CustomMouseListener(new CustomMouseListener.MouseClickListener() {
            @Override
            public void onMouseClick() {

                // TODO - Handle Color Click
                //listener.handleWorkingProfileColorClick(colorType, getWorkingProfileColor(colorType));
            }
        });
    }


    /*--- Listener Interface ---*/

    interface ProfileAttributePanelListener {

        void handleWorkingProfileSaveClick(Profile newProfile);
    }
}
