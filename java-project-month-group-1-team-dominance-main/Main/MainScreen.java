package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MainScreen {
    private JFrame gameFrame; // when the game launches
    private JPanel mainMenuPanel; // where menu buttons reside
    private JPanel mainScreenPanel; // where rest of the menu screen resides
    private int pannelWidth = 1280; // MAX width
    private int pannelHeight = 720; // MAX length

    // To run the Main_Screen
    public MainScreen() {
        initialize();
    } // END Main_Screen

    // Where the Main_Screen feature live
    public void initialize() {
        gameFrame = new JFrame();
        gameFrame.setTitle("Spellbound Tempo Launcher");
        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // gameFrame.setSize(pannelWidth, pannelHeight); Game Screen can be made smaller
        gameFrame.setMinimumSize(new Dimension(pannelWidth, pannelHeight)); // Game screen will not get smaller
        gameFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // IMPORTANT: Makes screen auto full screen
        gameFrame.setLocationRelativeTo(null);

        // ------------------- MENU BUTTONS PANEL ------------------- //

        // --------> STEP 2: Create JPanel, houses objects for the screen ( LESSON 2 )
        mainMenuPanel = new JPanel();
        mainMenuPanel.setLayout(new BoxLayout(mainMenuPanel, BoxLayout.Y_AXIS));
        mainMenuPanel.setBorder(BorderFactory.createEmptyBorder(100, 20, 20, 5)); // Width, Right, Height, Left
        mainMenuPanel.setOpaque(false); // Important: allows background to show through
        // mainMenuPanel.setBackground(Color.BLUE); // REMOVE TO SEE THE BACKGROUND
        mainMenuPanel.setPreferredSize(new Dimension(400, pannelHeight)); // Sets preferred width and height

        // ------------------- MAIN MENU PANEL ------------------- //
        // ⭐ --------> EXTRA FLAIR: Adding an extra panel ( LESSON 2 )
        mainScreenPanel = new JPanel(); 
        //mainScreenPanel.setBackground(Color.lightGray); // REMOVE TO SEE THE BACKGROUND
        mainScreenPanel.setOpaque(false); // Important: allows background to show through

        // --------> JAVA SWING - LESSON 7 - MODDING: Adding Button Images
        // --------> STEP 1: Create the buttons
        JButton gameScreenButton = createButton(
<<<<<<< HEAD
                "resources/ButtonSprites/NewGameButton.png",
                "resources/ButtonSprites/NewGameButtonHover.png",
=======
                "resources/NewGameButton.png",
                "resources/NewGameButtonHover.png",
>>>>>>> JaceS
        		" N E W  G A M E ",
        		KeyEvent.VK_R,
        		"New Game Button", 
        		"New Game Button Clicked!"
        		);

        // --------> STEP 3: Give the GAME BUTTON some action 
	    // --------> 🔥Specific location where the button goes
        // --------> 🖥️ This action takes you to a new screen
        gameScreenButton.addActionListener(e -> {
            System.out.println("New Game Button Clicked!");
            new GameScreen().setVisible(true); // Opens the new screen
            gameFrame.dispose(); // Closes the main menu
        });

        // --------> STEP 3: Give the SETTING BUTTON some action 

        JButton ContinueScreenButton = createButton(
<<<<<<< HEAD
            "resources/ButtonSprites/ContinueButton.png",
            "resources/ButtonSprites/ContinueButtonHover.png",
=======
            "resources/ContinueButton.png",
            "resources/ContinueButtonHover.png",
>>>>>>> JaceS
            " C O N T I N U E ",
            KeyEvent.VK_N,
            "Continue Button",
            "Continue Button Clicked!"
        );

        JButton SettingScreenButton = createButton(
<<<<<<< HEAD
            "resources/ButtonSprites/MenuButton.png",
            "resources/ButtonSprites/MenuButtonHover.png",
=======
            "resources/MenuButton.png",
            "resources/MenuButtonHover.png",
>>>>>>> JaceS
            " M E N U ",
            KeyEvent.VK_M,
            "Menu Button",
            "Menu Button Clicked!"
        );

        // --------> STEP 3: Give the MENU BUTTON some action 
	    // --------> 🔥Specific location where the button goes
        // --------> 🖥️ This action takes you to a new screen
        SettingScreenButton.addActionListener(e -> {
            System.out.println("Menu Button Clicked!");
            new SettingsScreen().setVisible(true); // Opens the new screen
            gameFrame.dispose(); // Closes the main menu
        });

        JButton creditScreenButton = createButton(
<<<<<<< HEAD
            "resources/ButtonSprites/CreditButton.png",
            "resources/ButtonSprites/CreditButtonHover.png",
=======
            "resources/CreditButton.png",
            "resources/CreditButtonHover.png",
>>>>>>> JaceS
        	" C R E D I T S ",
        	KeyEvent.VK_C,
        	"Credits Button", 
    		"Credits Button Clicked!"
    		);

        // --------> STEP 3: Give the CREDITS BUTTON some action 
	    // --------> 🔥Specific location where the button goes
        // --------> 🖥️ This action takes you to a new screen
        creditScreenButton.addActionListener(e -> {
            System.out.println("Credits Button Clicked!");
            new CreditsScreen().setVisible(true); // Opens the new screen
            gameFrame.dispose(); // Closes the main menu
        });

        JButton quitScreenButton = createButton(
<<<<<<< HEAD
            "resources/ButtonSprites/QuitButton.png",
            "resources/ButtonSprites/QuitButtonHover.png",
=======
            "resources/QuitButton.png",
            "resources/QuitButtonHover.png",
>>>>>>> JaceS
            " Q U I T ",
            KeyEvent.VK_Q,
            "Quit Button",
            "Quit Button Clicked!"
        );
        quitScreenButton.addActionListener(e -> {
            System.out.println("Quit Button Clicked!");
            System.exit(0);
        });

        // --------> STEP 2: Add the buttons to the menu panel    
        mainMenuPanel.add(gameScreenButton);
        mainMenuPanel.add(ContinueScreenButton);
        mainMenuPanel.add(SettingScreenButton);
        mainMenuPanel.add(creditScreenButton);
        mainMenuPanel.add(quitScreenButton);
        
        // --------> JAVA SWING - LESSON 8 - MODDING: Adding Background Images
  	
        // ------------------- BACKGROUND PANEL ( IMAGE )------------------- //    
        JPanel backgroundPanelMainScreen = new JPanel() {
            private Image backgroundImage = new ImageIcon("resources/FillerPicture.png").getImage(); // update this with MC image
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        
        backgroundPanelMainScreen.setLayout(new BorderLayout());

        // --------> JAVA SWING - LESSON 8 - MODDING: Adding Background Images
        backgroundPanelMainScreen.add(mainMenuPanel, BorderLayout.WEST); // for buttons
        backgroundPanelMainScreen.add(mainScreenPanel, BorderLayout.CENTER);

	    // Add background to the MainScreen
        gameFrame.setContentPane(backgroundPanelMainScreen); // -------> UPDATE w/ LESSON 8      
      
        show();
    } // END Intialize

     private JButton createButton(String imagePath, String hoverPath, String buttonText, int mnemonicKey, String toolTipMessage, String clickMessage) {

        int buttonWidth = 313; 
        int buttonHeight = 94;

        ImageIcon buttonImage = new ImageIcon(imagePath);
        Image scaledImage = buttonImage.getImage().getScaledInstance(buttonWidth,buttonHeight, Image.SCALE_SMOOTH); 
        ImageIcon buttonIcon = new ImageIcon(scaledImage); 

        ImageIcon buttonImageHover = new ImageIcon(hoverPath);
        Image scaledImageHover = buttonImageHover.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH); 
        ImageIcon buttonIconHover = new ImageIcon(scaledImageHover); 

        JButton button = new JButton(buttonIcon);
        button.setText(buttonText); 
        button.setBackground(Color.WHITE);
        button.setPreferredSize(new Dimension(buttonWidth + 5, buttonHeight + 5));
        
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setHorizontalAlignment(SwingConstants.CENTER);

        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setText(null);          // no hidden text
        button.setFocusable(false);    // no focus dots

        button.setMnemonic(mnemonicKey);
        button.setToolTipText(toolTipMessage);
        button.addActionListener(e -> {
            //button.setIcon(buttonIconClicked);
            System.out.println(clickMessage);
        });

	    // --------> 👔 BUTTON STYLE
	    button.setFont(new Font("Times New Roman", Font.PLAIN, 24)); // Button Font
	    button.setMargin(new Insets(10, 10, 80, 10)); // Button spacing from other buttons
	    
	    // --------> 😈 BUTTON ACTION🔥// Univeral Action Behavior - Styling / Effects
        // Hover effect (italic + cursor change)
	    button.addMouseListener(new java.awt.event.MouseAdapter() {
	        @Override
	        public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setIcon(buttonIconHover);
	            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
	            button.setFont(button.getFont().deriveFont(Font.ITALIC | Font.BOLD));
	        }
	        @Override
	        public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setIcon(buttonIcon);
	            button.setForeground(Color.WHITE);
	            button.setFont(button.getFont().deriveFont(Font.BOLD));
	        }
	    });
	    return button;
	}// END CREATE BUTTON
    
    public void show() {
        this.gameFrame.setVisible(true);
    } // END show

    public void setVisible(boolean b) {
        this.gameFrame.setVisible(b);
    } // END OF SHOWING CURRENT SCREEN
}