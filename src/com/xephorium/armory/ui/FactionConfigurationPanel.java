package com.xephorium.armory.ui;

import com.xephorium.armory.model.Faction;
import com.xephorium.armory.model.Profile;
import com.xephorium.armory.ui.resource.color.ArmoryColor;
import com.xephorium.armory.ui.resource.dimension.ArmoryDimension;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import static com.xephorium.armory.ui.resource.image.ArmoryImage.ICON_COVENANT;
import static com.xephorium.armory.ui.resource.image.ArmoryImage.ICON_UNSC;

public class FactionConfigurationPanel extends JPanel {


    /*--- Variables ---*/

    private enum ButtonType {
        RESET,
        SAVE
    }

    private FactionConfigurationPanelListener listener;

    private JComboBox[] unscComboBoxes = new JComboBox[6];
    private JComboBox[] covenantComboBoxes = new JComboBox[6];
    private String[] profileNames = {"No Profiles Available"};


    /*--- Constructor ---*/

    public FactionConfigurationPanel(FactionConfigurationPanelListener listener) {
        this.listener = listener;

        initializePanelAttributes();
        createTabbedPane();
    }


    /*--- Public Methods ---*/

    public void updateProfiles(Profile[] profiles) {

        String[] newProfileNames = new String[profiles.length];
        for (int x = 0; x < profiles.length; x++) {
            newProfileNames[x] = profiles[x].getName();
        }

        updateUNSCComboBoxes(newProfileNames);
        updateCovenantComboBoxes(newProfileNames);

        this.profileNames = newProfileNames;
    }


    /*--- Private Update Methods ---*/

    private void updateUNSCComboBoxes(String[] newProfileNames) {

        for (JComboBox comboBox : this.unscComboBoxes) {
            String oldSelection = this.profileNames[comboBox.getSelectedIndex()];
            comboBox.removeAllItems();
            for (int y = 0; y < newProfileNames.length; y++) {
                comboBox.addItem(newProfileNames[y]);
                if (newProfileNames[y].equals(oldSelection)) {
                    comboBox.setSelectedIndex(y);
                }
            }
        }
    }

    private void updateCovenantComboBoxes(String[] newProfileNames) {

        for (JComboBox comboBox : this.covenantComboBoxes) {
            String oldSelection = this.profileNames[comboBox.getSelectedIndex()];
            comboBox.removeAllItems();
            for (int y = 0; y < newProfileNames.length; y++) {
                comboBox.addItem(newProfileNames[y]);
                if (newProfileNames[y].equals(oldSelection)) {
                    comboBox.setSelectedIndex(y);
                }
            }
        }
    }


    /*--- Private Setup Methods ---*/

    private void initializePanelAttributes() {
        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(
                ArmoryDimension.PANEL_PADDING/2 - 3,
                ArmoryDimension.WINDOW_PADDING_HORIZONTAL,
                ArmoryDimension.WINDOW_PADDING_VERTICAL,
                ArmoryDimension.PANEL_PADDING/2 - 2));
        this.setBackground(ArmoryColor.WINDOW_BACKGROUND_COLOR);
    }

    private void createTabbedPane() {

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel unscPanel = new JPanel();
        unscPanel.setBackground(Color.WHITE);
        unscPanel.setLayout(new BorderLayout());
        unscPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        unscPanel.add(createPlayerListPanel(Faction.UNSC), BorderLayout.NORTH);
        unscPanel.add(createFactionPanel(Faction.UNSC), BorderLayout.CENTER);

        JLabel unscLabel = new JLabel("UNSC");
        unscLabel.setHorizontalAlignment(JLabel.CENTER);
        unscLabel.setBorder(new EmptyBorder(4, 10, 3, 10));

        tabbedPane.addTab("UNSC", unscPanel);
        tabbedPane.setTabComponentAt(0, unscLabel);

        JPanel covenantPanel = new JPanel();
        covenantPanel.setBackground(Color.WHITE);
        covenantPanel.setLayout(new BorderLayout());
        covenantPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        covenantPanel.add(createPlayerListPanel(Faction.COVENANT), BorderLayout.NORTH);
        covenantPanel.add(createFactionPanel(Faction.COVENANT), BorderLayout.CENTER);

        JLabel covenantLabel = new JLabel("Covenant");
        covenantLabel.setHorizontalAlignment(JLabel.CENTER);
        covenantLabel.setBorder(new EmptyBorder(4, 3, 3, 3));

        tabbedPane.addTab("Covenant", covenantPanel);
        tabbedPane.setTabComponentAt(1, covenantLabel);

        this.add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createPlayerListPanel(Faction faction) {

        int nameLabelPadding = 10;
        int playerSeparationPadding = 15;

        JPanel playerListPanel = new JPanel();
        playerListPanel.setLayout(new BoxLayout(playerListPanel, BoxLayout.Y_AXIS));
        playerListPanel.setBackground(Color.WHITE);

        for (int playerNumber = 0; playerNumber < 6; playerNumber++) {

            JPanel playerPanel = new JPanel();
            playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.X_AXIS));
            playerPanel.setBorder(new EmptyBorder(0, 0, playerSeparationPadding, 0));
            playerPanel.setBackground(Color.WHITE);
            playerPanel.add(new JLabel("Player " + (playerNumber + 1)));
            playerPanel.add(new Box.Filler(
                    new Dimension(nameLabelPadding, 0),
                    new Dimension(nameLabelPadding, 0),
                    new Dimension(nameLabelPadding, 0)));

            if (faction == Faction.UNSC) {
                unscComboBoxes[playerNumber] = createFactionComboBox(Faction.UNSC, playerNumber);
                playerPanel.add(unscComboBoxes[playerNumber]);
            } else {
                covenantComboBoxes[playerNumber] = createFactionComboBox(Faction.COVENANT, playerNumber);
                playerPanel.add(covenantComboBoxes[playerNumber]);
            }

            playerListPanel.add(playerPanel);
        }

        return playerListPanel;
    }

    private JComboBox createFactionComboBox(Faction faction, int playerNumber) {

        JComboBox comboBox;
        comboBox = new JComboBox(profileNames);
        comboBox.setSelectedIndex(0);
        comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    if (faction == Faction.UNSC) {
                        listener.handleUNSCPlayerUpdate(playerNumber, profileNames[comboBox.getSelectedIndex()]);
                    } else {
                        listener.handleCovenantPlayerUpdate(playerNumber, profileNames[comboBox.getSelectedIndex()]);
                    }
                }
            }
        });
        return comboBox;
    }

    private JPanel createFactionPanel(Faction faction) {

        JPanel factionPanel = new JPanel(new BorderLayout());
        factionPanel.setLayout(new BoxLayout(factionPanel, BoxLayout.X_AXIS));
        factionPanel.setBackground(Color.WHITE);

        factionPanel.add(createFactionButtonPanel(ButtonType.RESET, faction), BorderLayout.LINE_START);
        factionPanel.add(createFactionIconPanel(faction), BorderLayout.CENTER);
        factionPanel.add(createFactionButtonPanel(ButtonType.SAVE, faction), BorderLayout.LINE_END);

        return factionPanel;
    }

    private JPanel createFactionButtonPanel(ButtonType buttonType, Faction faction) {

        JPanel mainButtonPanel = new JPanel(new BorderLayout());
        mainButtonPanel.setBackground(Color.WHITE);

        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEADING, 0, 0);
        flowLayout.setAlignment(buttonType == ButtonType.RESET ? FlowLayout.LEFT : FlowLayout.RIGHT);

        JPanel horizontalButtonPanel = new JPanel(flowLayout);
        horizontalButtonPanel.setBackground(Color.WHITE);

        JButton button = new JButton(buttonType == ButtonType.RESET ? "Reset" : "Save");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (buttonType == ButtonType.RESET) {
                    if (faction == Faction.UNSC) {
                        listener.handleUNSCConfigurationReset();
                    } else {
                        listener.handleCovenantConfigurationReset();
                    }
                } else {
                    if (faction == Faction.UNSC) {
                        listener.handleUNSCConfigurationSave();
                    } else {
                        listener.handleCovenantConfigurationSave();
                    }
                }
            }
        });

        horizontalButtonPanel.add(button);
        mainButtonPanel.add(horizontalButtonPanel, BorderLayout.SOUTH);

        return mainButtonPanel;
    }

    private JPanel createFactionIconPanel(Faction faction) {

        JPanel iconPanel = new JPanel(new GridLayout(1, 1));
        iconPanel.setBackground(Color.WHITE);
        iconPanel.add(new JLabel("", faction == Faction.UNSC ? ICON_UNSC : ICON_COVENANT, JLabel.CENTER));
        return iconPanel;
    }


    /*--- Listener Interface ---*/

    interface FactionConfigurationPanelListener {

        void handleUNSCPlayerUpdate(int playerNumber, String profileName);

        void handleCovenantPlayerUpdate(int playerNumber, String profileName);

        void handleUNSCConfigurationReset();

        void handleCovenantConfigurationReset();

        void handleUNSCConfigurationSave();

        void handleCovenantConfigurationSave();
    }
}