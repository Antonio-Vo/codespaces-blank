package Main;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsScreen extends JPanel {

    private JFrame gameFrame; // when the game launches
    private JPanel menuMenuPanel; // menu where game resides
    private JPanel menuScreenPanel;
    private JPanel buttonWrapper;
    private JButton backToMainButton;
    private int FRAME_WIDTH = 1280;
    private int FRAME_HEIGHT = 720;

    public SettingsScreen() {

    initialize();

    // Example placeholder panel for words
        JLabel label = new JLabel("This is the Credits Screen", SwingConstants.CENTER);
        label.setFont(new Font("Times New Roman", Font.BOLD, 28));
        add(label, BorderLayout.CENTER);

    } // END Credits_Screen

    public void initialize() {
            gameFrame = new JFrame();
            gameFrame.setTitle("Menu");
            gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            gameFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
            gameFrame.setLocationRelativeTo(null);
            gameFrame.setLayout(new BorderLayout());

            //-------------------- GAME MENU PANEL -------------//
            menuMenuPanel = new JPanel(); // initialize the panel so it can be seen
            menuScreenPanel = new JPanel(new BorderLayout());
            menuMenuPanel.setLayout(new BoxLayout(menuMenuPanel, BoxLayout.Y_AXIS));
            menuMenuPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Width | Right | Height | Left
            // gameMenuPanel.setOpaque(false); // Need for the background to be seen
            menuMenuPanel.setBackground(Color.YELLOW); // remove if background is set up

            //-------------- Connect Main_Screen to Game_Screen ----------------//
            // 1. Intitialize the button field using the helper method
            backToMainButton = createBackToMainButton();
            buttonWrapper = new JPanel();
            buttonWrapper.add(backToMainButton);

            // 2. Add the ActionListener, calling the new function
            backToMainButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // call method for the main screen
                    switchToMain_Screen(backToMainButton);
                }
            });

            menuScreenPanel.setBackground(Color.WHITE);
            menuScreenPanel.setFocusable(true);
            // menuScreenPanel.addKeyListener((KeyListener) this); // add key listener only to the GamePanel

            //------------------ ADD CONTENT TO FRAME --------------//
            gameFrame.setLayout(new BorderLayout());
            gameFrame.add(this, BorderLayout.CENTER);

            // Places the wrapper to the left of the screen
            gameFrame.add(buttonWrapper, BorderLayout.WEST);

            // Game area (CENTER fills remaining space)
            gameFrame.add(menuScreenPanel, BorderLayout.CENTER);

            //---------- Adding Player_Sprite to Game_Screen --------//
            this.setBackground(Color.WHITE);

            gameFrame.setVisible(true);

    } // END Initialize

//------------ Connect Main_Screen to Menu_Screen -------------//
    private void switchToMain_Screen(JButton sourceButton) {
        // Open the Main_Screen
        MainScreen runMain_Screen = new MainScreen();
        runMain_Screen.setVisible(true);

        // Dispose of the current frame0
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(sourceButton);
        if (currentFrame != null) {
            currentFrame.dispose();
        }
    } // END switchToMain

    public JButton createBackToMainButton() {
        JButton button = new JButton("Back to Main");
        button.setFocusable(false);
        return button;
    } // END of backToMainButton

    public void setVisible(boolean b) {
        this.gameFrame.setVisible(b);
        SwingUtilities.invokeLater(() -> menuScreenPanel.requestFocusInWindow());
    } // END OF SHOWING CURRENT SCREEN

} // END Class Credits_Screen