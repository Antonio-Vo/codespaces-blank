
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
import java.awt.event.MouseEvent; //--added for mouse clicking attack
import java.awt.event.MouseAdapter; //--added for mouse clicking attack

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameScreen extends JPanel implements ActionListener, KeyListener {

    private JFrame gameFrame; // when the game launches
    private JPanel gameMenuPanel; // menu where game resides
    private JPanel gameScreenPanel;
    private JPanel buttonWrapper;
    private JButton backToMainButton;

    
     boolean attacking = false; //Player Attacking 
      boolean Shielding = false; //Player Shielding

    private int FRAME_WIDTH = 1280;
    private int FRAME_HEIGHT = 720;
    
    private final int MAP_COLS = 20; // number of tiles horizontally
    private final int MAP_ROWS = 10; // number of tiles vertically

    private Health_Bar healthBar;

    private ChestSprite chest;

    private BufferedImage chestClosed;
    private BufferedImage[] chestOpenFrames;

    private PotionCounter potionCounter; //POTION LOGIC
    private BufferedImage[] potionCounterSprites; //POTION LOGIC

    //--------- Add Player_Sprite to Game_Screen -------------//
    Timer t = new Timer(10, this);
    PlayerSprite p = new PlayerSprite(0, 0, 68, 68, -0, -0);
    EnemySprite enemy = new EnemySprite(150, 150, 20, 20);
    BasherEnemySprite basher = new BasherEnemySprite(350, 350, 20, 20);
    // int playerHealth = 10;

    // Global movement timer so all sprites move synchronously
    private long movementLastTime = 0;
    private long movementTickCount = 0;
  
    private BufferedImage backgroundImage;

    public GameScreen() {

        //--------- Add Player_Sprite to Game_Screen -------------//
        t.start();

        //-------------- Set up the Game_Screen --------------//
        initialize();
        // Basher should act every 2 movement ticks
        basher.setTickInterval(2);

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
                 // Draw chest
                if (chest != null) {
                    chest.draw(g);
                }

                if (chest != null && !chest.isOpened() && chest.isPlayerNearby(p)) {
                    g.setColor(Color.WHITE);
                    g.fillRect(chest.x, chest.y - 20, 90, 16);
                    g.setColor(Color.BLACK);
                    g.drawString("Press E", chest.x + 10, chest.y - 8);
                }

                
                p.draw(g);
                enemy.draw(g);
                basher.draw(g);
            }
        };

        // Load the image (add this in initialize() after creating gameScreenPanel)
        try {
            backgroundImage = ImageIO.read(new File("resources/TQTM_Area2Small.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // chest image load
        try {
            chestClosed = ImageIO.read(new File("resources/ChestSprite(Closed-Left).png"));

            chestOpenFrames = new BufferedImage[] {
                ImageIO.read(new File("resources/ChestSprite(HalfOpen-Left).png")),
                ImageIO.read(new File("resources/ChestSprite(FullyOpen-Left).png")),
                ImageIO.read(new File("resources/ChestSprite(FullyOpenEmpty-Left)-4.png.png")),
                ImageIO.read(new File("resources/ChestSprite(HalfOpenEmpty-Left)-5.png.png")),
                ImageIO.read(new File("resources/ChestSprite(Closed-Left).png"))
<<<<<<< HEAD
            chestClosed = ImageIO.read(new File("resources/ChestSprites/ChestSprite(Closed-Left).png"));

            chestOpenFrames = new BufferedImage[] {
                ImageIO.read(new File("resources/ChestSprites/ChestSprite(HalfOpen-Left).png")),
                ImageIO.read(new File("resources/ChestSprites/ChestSprite(FullyOpen-Left).png")),
                ImageIO.read(new File("resources/ChestSprites/ChestSprite(FullyOpenEmpty-Left)-4.png.png")),
                ImageIO.read(new File("resources/ChestSprites/ChestSprite(HalfOpenEmpty-Left)-5.png.png")),
                ImageIO.read(new File("resources/ChestSprites/ChestSprite(Closed-Left).png"))
            };
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Potion counter image load
        try {
            potionCounterSprites = new BufferedImage[16];

=======
            };
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Potion counter image load
        try {
            potionCounterSprites = new BufferedImage[16];

>>>>>>> JaceS
            for (int i = 0; i < 16; i++) {
                potionCounterSprites[i] = ImageIO.read( //POTION LOGIC
                    new File("resources/PotionCounter/PotionCounter(" + i + ").png")
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        gameScreenPanel.setBackground(Color.WHITE); // fallback if image fails to load
        gameScreenPanel.setFocusable(true);
        gameScreenPanel.addKeyListener(this); // add key listener only to the GamePanel

        healthBar = new Health_Bar(p); // p is your Player_Sprite
        healthBar.setBounds(10, 10, 200, 40); // position top-left
        gameScreenPanel.setLayout(null);       // absolute positioning
        gameScreenPanel.add(healthBar);
        gameScreenPanel.setBackground(Color.BLUE); // or transparent if needed

        potionCounter = new PotionCounter(p, potionCounterSprites); //POTION LOGIC
        potionCounter.setBounds(220, 10, 40, 40); // right of health bar //POTION LOGIC
        gameScreenPanel.add(potionCounter); //POTION LOGIC

        //------------------ ADD CONTENT TO FRAME --------------//
        gameFrame.setLayout(new BorderLayout());

        // Places the wrapper to the left of the screen
        // gameFrame.add(buttonWrapper, BorderLayout.WEST);

        // Game area (CENTER fills remaining space)
        gameFrame.add(gameScreenPanel, BorderLayout.CENTER);

        //---------- Adding Player_Sprite to Game_Screen --------//
        this.setBackground(Color.WHITE);

        //this is where You will add player ATTACK !!!!!!!!
        gameScreenPanel.setFocusable(true);
        gameScreenPanel.requestFocusInWindow();

        gameScreenPanel.addMouseListener(new MouseAdapter() {

            //------Player Attacking on the left side and the Player Shielding when clicking on the Right
            @Override
            public void mousePressed(MouseEvent e){
                if (SwingUtilities.isLeftMouseButton(e)){
                    // single attack per click
                    System.out.println("Left Click Detected: Player ATTACK !!!");
                    p.attack(); // animation / feedback
                    performPlayerAttack();
                }
                if(SwingUtilities.isRightMouseButton(e)){
                    Shielding = true;
                    p.setShielding(true);
                    p.shield();
                    System.out.println("Right Click Detected: Player SHIELD !!!");
                }
            }

            @Override
            public void mouseReleased(MouseEvent e){
                if(SwingUtilities.isLeftMouseButton(e))
                    attacking = false;

                if(SwingUtilities.isRightMouseButton(e)) {
                    Shielding = false;
                    p.setShielding(false);
                }
            }

        });

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
        // match current tile size based on panel size so movement scales with window
        int panelW = Math.max(1, gameScreenPanel.getWidth());
        int panelH = Math.max(1, gameScreenPanel.getHeight());
        int tileW = Math.max(1, panelW / MAP_COLS);
        int tileH = Math.max(1, panelH / MAP_ROWS);

        //
        if (chest == null) {
            chest = new ChestSprite(
                8,   // column
                5,   // row
                tileW,
                tileH,
                chestClosed,
                chestOpenFrames
            );
        }

        if (chest != null) {
            chest.tick();
        }

        if (potionCounter != null) { //POTION LOGIC
            potionCounter.refresh();
        }

        // If player sprite size doesn't match the tile size, update it and snap to grid
        if (p.width != tileW || p.height != tileH) {
            p.setSize(tileW, tileH);
            p.snapToGrid(tileW, tileH);
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - movementLastTime >= PlayerSprite.MOVE_DELAY) {
            movementTickCount++;

            // Track occupied tiles as "x,y" strings so sprites don't move into each other
            java.util.Set<String> occupied = new java.util.HashSet<>();
            int pTileX = p.x / tileW, pTileY = p.y / tileH;
            occupied.add(pTileX + "," + pTileY);
            int enemyTileX = enemy.x / tileW, enemyTileY = enemy.y / tileH;
            occupied.add(enemyTileX + "," + enemyTileY);
            int basherTileX = basher.x / tileW, basherTileY = basher.y / tileH;
            occupied.add(basherTileX + "," + basherTileY);

            // --- Player move (attempt) ---
            int pdx = p.getDx(), pdy = p.getDy();
            if (pdx != 0 || pdy != 0) {
                int nextPx = p.x + pdx;
                int nextPy = p.y + pdy;
                // bounds check
                if (nextPx >= 0 && nextPx + p.width <= panelW && nextPy >= 0 && nextPy + p.height <= panelH) {
                    int targetPTileX = nextPx / tileW;
                    int targetPTileY = nextPy / tileH;
                    String key = targetPTileX + "," + targetPTileY;
                    if (!occupied.contains(key)) {
                        p.x = nextPx; p.y = nextPy;
                        occupied.remove(pTileX + "," + pTileY);
                        occupied.add(key);
                    }
                }
                p.setDx(0); p.setDy(0);
            }

            // --- Enemy (generic) move (attempt respecting its tick interval) ---
            if (movementTickCount % enemy.getTickInterval() == 0) {
                java.awt.Point step = enemy.getIntendedStep(p);
                if (step.x != 0 || step.y != 0) {
                    int nextEx = enemy.x + step.x;
                    int nextEy = enemy.y + step.y;
                    if (nextEx >= 0 && nextEx + enemy.width <= panelW && nextEy >= 0 && nextEy + enemy.height <= panelH) {
                        int targetETileX = nextEx / tileW;
                        int targetETileY = nextEy / tileH;
                        String key = targetETileX + "," + targetETileY;
                        String playerKey = pTileX + "," + pTileY;
                        // If enemy tries to enter the player's tile, damage player but don't move
                        if (key.equals(playerKey)) {
                            p.takeDamage(enemy.getDamage());
                            healthBar.updateHealthBar();
                            System.out.println("Player by enemy: " + p.getCurrentHealth());
                        } else if (!occupied.contains(key)) {
                            enemy.x = nextEx; enemy.y = nextEy;
                            occupied.remove(enemyTileX + "," + enemyTileY);
                            occupied.add(key);
                        }
                    }
                }
            }

            // --- Basher move (attempt) ---
            if (movementTickCount % basher.getTickInterval() == 0) {
                java.awt.Point step = basher.getIntendedStep(p);
                if (step.x != 0 || step.y != 0) {
                    int nextBx = basher.x + step.x;
                    int nextBy = basher.y + step.y;
                    if (nextBx >= 0 && nextBx + basher.width <= panelW && nextBy >= 0 && nextBy + basher.height <= panelH) {
                        int targetBTileX = nextBx / tileW;
                        int targetBTileY = nextBy / tileH;
                        String key = targetBTileX + "," + targetBTileY;
                        String playerKey = pTileX + "," + pTileY;
                        // If basher tries to enter the player's tile, damage player but don't move
                        if (key.equals(playerKey)) {
                            p.takeDamage(basher.getDamage());
                            healthBar.updateHealthBar();
                            System.out.println("Player damaged by basher attempting to step: " + p.getCurrentHealth());
                        } else if (!occupied.contains(key)) {
                            basher.x = nextBx; basher.y = nextBy;
                            occupied.remove(basherTileX + "," + basherTileY);
                            occupied.add(key);
                        }
                    }
                }
            }

            movementLastTime = currentTime;
        }
        gameScreenPanel.repaint(); // re draws the player after movement

        if (enemy.collidesWith(p)) {
            p.takeDamage(enemy.getDamage());
            healthBar.updateHealthBar();
            System.out.println("Player hit by enemy! Health: " + healthBar);
        }
        if (basher.collidesWith(p)) {
            p.takeDamage(basher.getDamage());
            healthBar.updateHealthBar();
            System.out.println("Player hit by basher! Health: " + healthBar);
        }
        System.out.println("position is: " + p.x + "," + p.y);

        //------Player Attacking on the left side and the Player Shielding when clicking on the Right
        if(attacking){
            p.attack();
        }
        if (Shielding) {
            p.shield();
        }

    } // END actionPerformed(ActionEvent e)


     private boolean willCollideWithChest(int nextX, int nextY) {
        if (chest == null) return false;

        Rectangle nextPlayerBox =
            new Rectangle(nextX, nextY, p.width, p.height);

        return nextPlayerBox.intersects(chest.getHitbox());
    }

    // Perform an attack based on player's facing and attack mode
    private void performPlayerAttack() {
        int panelW = Math.max(1, gameScreenPanel.getWidth());
        int panelH = Math.max(1, gameScreenPanel.getHeight());
        int tileW = Math.max(1, panelW / MAP_COLS);
        int tileH = Math.max(1, panelH / MAP_ROWS);

        Rectangle attackRect = null;
        int damage = 0;

        switch (p.getAttackMode()) {
            case PlayerSprite.ATTACK_SWIPE:
            default:
                damage = 10;
                if (p.getFacing() == PlayerSprite.FACING_RIGHT) {
                    attackRect = new Rectangle(p.x + p.width, p.y, tileW, tileH);
                } else if (p.getFacing() == PlayerSprite.FACING_LEFT) {
                    attackRect = new Rectangle(p.x - tileW, p.y, tileW, tileH);
                } else if (p.getFacing() == PlayerSprite.FACING_UP) {
                    attackRect = new Rectangle(p.x, p.y - tileH, tileW, tileH);
                } else if (p.getFacing() == PlayerSprite.FACING_DOWN) {
                    attackRect = new Rectangle(p.x, p.y + p.height, tileW, tileH);
                }
                break;
        }

        if (attackRect == null) return;

        System.out.println("Player attack area: " + attackRect + " damage: " + damage);

        if (attackRect.intersects(enemy)) {
            enemy.takeDamage(damage);
            System.out.println("Hit enemy for " + damage + " damage. Enemy HP: " + enemy.getCurrentHealth() + "/" + enemy.getMaxHealth());
            if (enemy.getCurrentHealth() <= 0) {
                enemy.setBounds(-1000, -1000, 0, 0);
                System.out.println("Enemy defeated!");
            }
        }

        if (attackRect.intersects(basher)) {
            basher.takeDamage(damage);
            System.out.println("Hit basher for " + damage + " damage. Basher HP: " + basher.getCurrentHealth() + "/" + basher.getMaxHealth());
            if (basher.getCurrentHealth() <= 0) {
                basher.setBounds(-1000, -1000, 0, 0);
                System.out.println("Basher defeated!");
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        p.draw(g);
        enemy.draw(g);
        basher.draw(g);
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
                if (!willCollideWithChest(p.x, p.y - tileH))
                    p.setDy(-tileH);
                    p.setFacing(PlayerSprite.FACING_UP);
                break;
            case KeyEvent.VK_A: // left
                // If moving left would cross the left boundary, don't set movement
                if (p.x - tileW < 0) break;
                if (!willCollideWithChest(p.x - tileW, p.y))
                    p.setDx(-tileW);
                    p.setFacing(PlayerSprite.FACING_LEFT);
                break;
            case KeyEvent.VK_S: // down
                // If moving down would cross the bottom boundary, don't set movement
                if (p.y + p.height + tileH > panelH) break;
                if (!willCollideWithChest(p.x, p.y + tileH))
                    p.setDy(tileH);
                    p.setFacing(PlayerSprite.FACING_DOWN);
                break;
            case KeyEvent.VK_D: // right
                // If moving right would cross the right boundary, don't set movement
                if (p.x + p.width + tileW > panelW) break;
                if (!willCollideWithChest(p.x + tileW, p.y))
                    p.setDx(tileW);
                    p.setFacing(PlayerSprite.FACING_RIGHT);
                break;
            case KeyEvent.VK_E:
                if (chest != null && !chest.isOpened() && chest.isPlayerNearby(p)) {
                    chest.open(p);
                }
                break;
            case KeyEvent.VK_R: //POTION LOGIC
                if (p.usePotion()) {
                    healthBar.updateHealthBar();
                    potionCounter.refresh();
                }
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
