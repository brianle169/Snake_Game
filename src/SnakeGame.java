import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SnakeGame implements ActionListener {

    public JFrame mainMenu;
    public JLabel welcomeLabel;
    public JLabel selectionLabel;
    public JLabel difficultyLabel;
    public JLabel wallCollisionLabel;
    public JPanel menuPanel;
    public JComboBox difficulties;
    public JComboBox wallCollision;
    public String[] difficultyOptions = {"Easy", "Moderate", "Hard", "Impossible"};
    public String[] wallCollisionOptions = {"On", "Off"};
    public JButton startButton;
    public String difficulty;
    public String wallCollisionActivation;
    public final int MENU_WIDTH = Constants.PANEL_WIDTH;
    public final int MENU_HEIGHT = Constants.PANEL_HEIGHT;

    public SnakeGame(){
        mainMenu = new JFrame();
        mainMenu.setTitle("Snake Game Main Menu");
        mainMenu.setSize(MENU_WIDTH, MENU_HEIGHT);
        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(MENU_WIDTH,MENU_HEIGHT));
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setLayout(null);

        menuPanel.add(createWelcomeLabel());
        menuPanel.add(createSelectionLabel());
        menuPanel.add(createComboBox());
        menuPanel.add(createWallCollisionBox());
        menuPanel.add(createDifficultyLabel());
        menuPanel.add(createWallCollisionLabel());
        menuPanel.add(createStartButton());

        mainMenu.add(menuPanel);
        mainMenu.setResizable(false);
        mainMenu.pack();
        mainMenu.setLocationRelativeTo(null);
        mainMenu.setVisible(true);
    }


    public JLabel createWelcomeLabel(){
        welcomeLabel = new JLabel("Welcome to Snaky Snaky!");
        welcomeLabel.setFont(new Font(Font.MONOSPACED,Font.BOLD,25));
        welcomeLabel.setForeground(Color.BLACK);
        welcomeLabel.setBounds(120,100,400,30);
        return welcomeLabel;
    }

    public JLabel createDifficultyLabel(){
        difficultyLabel = new JLabel("Difficulty");
        difficultyLabel.setFont(new Font(Font.MONOSPACED,Font.BOLD,18));
        difficultyLabel.setForeground(Color.BLACK);
        difficultyLabel.setBounds(120,270,150,30);
        return difficultyLabel;
    }

    public JLabel createWallCollisionLabel(){
        wallCollisionLabel = new JLabel("Wall Collision");
        wallCollisionLabel.setFont(new Font(Font.MONOSPACED,Font.BOLD,18));
        wallCollisionLabel.setForeground(Color.BLACK);
        wallCollisionLabel.setBounds(315,270,250,30);
        return wallCollisionLabel;
    }

    public JLabel createSelectionLabel(){
        String text = "Configure your game";
        selectionLabel = new JLabel(text);
        selectionLabel.setFont(new Font(Font.MONOSPACED,Font.BOLD,22));
        selectionLabel.setForeground(Color.BLACK);
        selectionLabel.setBounds(170,200,600,30);
        return selectionLabel;
    }

    public JComboBox createComboBox(){
        difficulties = new JComboBox();
        for (String option: difficultyOptions){
            difficulties.addItem(option);
        }
        difficulties.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,20));
        difficulties.setBounds(120,300,150,30);
        difficulties.setEditable(false);
        return difficulties;
    }

    public JComboBox createWallCollisionBox(){
        wallCollision = new JComboBox();
        for (String option: wallCollisionOptions){
            wallCollision.addItem(option);
        }
        wallCollision.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,20));
        wallCollision.setBounds(315,300,150,30);
        wallCollision.setEditable(false);
        return wallCollision;
    }

    public JButton createStartButton(){
        startButton = new JButton("Start");
        startButton.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,20));
        startButton.setBounds(215,375,150,30);
        startButton.addActionListener(this);
        return startButton;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton){
            difficulty = (String) difficulties.getSelectedItem();
            wallCollisionActivation = (String) wallCollision.getSelectedItem();
        }
        mainMenu.dispose();
        switch (difficulty){
            case "Easy" -> new GamePanel(100, wallCollisionActivation);
            case "Moderate" -> new GamePanel(50, wallCollisionActivation);
            case "Hard" -> new GamePanel(20, wallCollisionActivation);
            case "Impossible" -> new GamePanel(10, wallCollisionActivation);
        }
    }
}


