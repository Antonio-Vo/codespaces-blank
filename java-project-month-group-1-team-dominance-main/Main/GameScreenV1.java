package Main;

import Player_Movement.*;  

// import Player_Movement.EnemySprite; // <- Import custom class made from a different package
// import Player_Movement.Player_Sprite; // <- Import custom class made from a different package

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameScreenV1 extends JPanel implements ActionListener, KeyListener {

    private JFrame gameFrame; // when the game launches
    private JPanel gameMenuPanel; // menu where game resides
    private JPanel gameScreenPanel;
    private JPanel buttonWrapper;
    private JButton backToMainButton;
    private int FRAME_WIDTH = 1280;
    private int FRAME_HEIGHT = 720;
    private final int MAP_COLS = 20; // number of tiles horizontally
    private final int MAP_ROWS = 10; // number of tiles vertically

    //--------- Add Player_Sprite to Game_Screen -------------//
    Timer t = new Timer(10, this);
    PlayerSprite p = new PlayerSprite(0, 0, 68, 68, -0, -0);
    EnemySprite enemy = new EnemySprite(200, 200, 20, 20);
    int playerHealth = 10;
    private BufferedImage backgroundImage;

    public GameScreenV1() {

        //--------- Add Player_Sprite to Game_Screen -------------//
        t.start();

        //-------------- Set up the Game_Screen --------------//
        initialize();

        // Example placeholder panel for words
        JLabel label = new JLabel("This is the New Game Screen", SwingConstants.CENTER);
        label.setFont(new Font("Times New Roman", Font.BOLD, 28));
        add(label, BorderLayout.CENTER);
    } // END Game_Screen

    public void initialize() {
        gameFrame = new JFrame();
        gameFrame.setTitle("Tempo Quest");
        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setLayout(new BorderLayout());

        //-------------------- GAME MENU PANEL -------------//
        gameMenuPanel = new JPanel(); // initialize the panel so it can be seen
        gameMenuPanel.setLayout(new BoxLayout(gameMenuPanel, BoxLayout.Y_AXIS));
        gameMenuPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Width | Right | Height | Left
        // gameMenuPanel.setOpaque(false); // Need for the background to be seen
        gameMenuPanel.setBackground(Color.YELLOW); // remove if background is set up

        //-------------- Connect Main_Screen to Game_Screen ----------------//

        //NOTE: TURN THIS INTO A "Esc" KEY EVENT
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

        //---------- Adding Player_Sprite to Game_Screen --------//
        gameScreenPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                

                int imgWidth = backgroundImage.getWidth();
                int imgHeight = backgroundImage.getHeight();
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), 0, 0, imgWidth , imgHeight , this);
                
                p.draw(g);
                enemy.draw(g);
            }
        };

        // Load the image (add this in initialize() after creating gameScreenPanel)
        try {
            backgroundImage = ImageIO.read(new File("resources/TQTM_Area2Small.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        gameScreenPanel.setBackground(Color.WHITE); // fallback if image fails to load
        gameScreenPanel.setFocusable(true);
        gameScreenPanel.addKeyListener(this); // add key listener only to the GamePanel

        //------------------ ADD CONTENT TO FRAME --------------//
        gameFrame.setLayout(new BorderLayout());

        // Places the wrapper to the left of the screen
        // gameFrame.add(buttonWrapper, BorderLayout.WEST);

        // Game area (CENTER fills remaining space)
        gameFrame.add(gameScreenPanel, BorderLayout.CENTER);

        //---------- Adding Player_Sprite to Game_Screen --------//
        this.setBackground(Color.WHITE);

    } // END intialize

    //------------ Connect Main_Screen to Game_Screen -------------//
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
        SwingUtilities.invokeLater(() -> gameScreenPanel.requestFocusInWindow());
    } // END OF SHOWING CURRENT SCREEN

    //----------- Adding Player_Sprite to Game_Screen --------------//
    @Override
    public void actionPerformed(ActionEvent e) {
        // Compute current tile size based on panel size so movement scales with window
        int panelW = Math.max(1, gameScreenPanel.getWidth());
        int panelH = Math.max(1, gameScreenPanel.getHeight());
        int tileW = Math.max(1, panelW / MAP_COLS);
        int tileH = Math.max(1, panelH / MAP_ROWS);

        // If player sprite size doesn't match the tile size, update it and snap to grid
        if (p.width != tileW || p.height != tileH) {
            p.setSize(tileW, tileH);
            p.snapToGrid(tileW, tileH);
        }

        p.tick(panelW, panelH); // keeps the player in bounds
        enemy.tick(p, panelW, panelH);
        gameScreenPanel.repaint(); // re draws the player after movement

        if (enemy.collidesWith(p)) {
            playerHealth -= enemy.getDamage();
            System.out.println("Player hit! Health: " + playerHealth);
        }
        System.out.println("position is: " + p.x + "," + p.y);

    } // END actionPerformed(ActionEvent e)

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        p.draw(g);
        enemy.draw(g);
    } // END paintComponent(Graphics g)

    @Override
    public void keyPressed(KeyEvent e) {
        // Implementaion of the WASD key press for character movement aligned to tiles
        int panelW = Math.max(1, gameScreenPanel.getWidth());
        int panelH = Math.max(1, gameScreenPanel.getHeight());
        int tileW = Math.max(1, panelW / MAP_COLS);
        int tileH = Math.max(1, panelH / MAP_ROWS);

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W: // up
                // If moving up would cross the top boundary, don't set movement
                if (p.y - tileH < 0) break;
                p.setDy(-tileH);
                break;
            case KeyEvent.VK_A: // left
                // If moving left would cross the left boundary, don't set movement
                if (p.x - tileW < 0) break;
                p.setDx(-tileW);
                break;
            case KeyEvent.VK_S: // down
                // If moving down would cross the bottom boundary, don't set movement
                if (p.y + p.height + tileH > panelH) break;
                p.setDy(tileH);
                break;
            case KeyEvent.VK_D: // right
                // If moving right would cross the right boundary, don't set movement
                if (p.x + p.width + tileW > panelW) break;
                p.setDx(tileW);
                break;
        } // END switch
    } // END keyPressed(KeyEvent e)

    @Override
    public void keyReleased(KeyEvent e) {
        // Implementaion of the WASD key press for character movement to STOP

         switch (e.getKeyCode()) {
            case KeyEvent.VK_W: // up
                p.setDy(0);
                break;
            case KeyEvent.VK_A: // left
                p.setDx(0);
                break;
            case KeyEvent.VK_S: // down
                p.setDy(0);
                break;
            case KeyEvent.VK_D: // right
                p.setDx(0);
                break;
        } // END switch

    } // END keyReleased(KeyEvent e)

    //----------- Camera movement ----------//
    // public class camera {
    //     private float x,y;
    //     private int viewWidth, viewHeight;
    //     private int worldWidth, worldHeight;

    //     public void Camera(int viewWidth, int viewHeight, int worldWidth, int worldHeight) {
    //     this.viewWidth = viewWidth;
    //     this.viewHeight = viewHeight;
    //     this.worldWidth = worldWidth;
    //     this.worldHeight = worldHeight;
        
    // }
    // }
    

    @Override
    public void keyTyped(KeyEvent e) {

    } // END keyTyped(KeyEvent e)

} // END class Game_Screen