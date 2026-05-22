package Player_Movement;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PlayerSprite extends Rectangle {

    // ======== HEATH BAR ======== // 
    private int currentHealth = 100;
    private int maxHealth = 100;

    // ======== PLAYER MOVEMENT ======== //
    private int dx, dy; // Character Velocity
    private long lastMoveTime = 0; // Track time of last movement
    public static final long MOVE_DELAY = 750; // 1 second in milliseconds
    private BufferedImage playerImage;
    private BufferedImage originalImage; // keep original to rescale when panel size changes

    public PlayerSprite(int x, int y, int width, int Height, int dx, int dy) {
        setBounds(x, y, width, Height);
        this.dx = dx;
        this.dy = dy;
        loadImage("PlayerImages/WALKING_RIGHT.png");
        // ensure initial scaling based on constructor width/height
        rescaleTo(width, height);
    } // END Player_Sprite

    //--------------- Potions --------------------//
    private int potions = 0; //POTION LOGIC
    private static final int HEAL_AMOUNT = 10; //POTION LOGIC

    public int getPotions() { //POTION LOGIC
        return potions;
    }

    public void addPotion() { //POTION LOGIC
        potions++;
        if (potions > 15) potions = 15; // cap at max sprite
        System.out.println("Potions: " + potions);
    }

    public boolean usePotion() { //POTION LOGIC
        if (potions <= 0) {
            System.out.println("No potions!");
            return false;
        }
        if (currentHealth >= maxHealth) { //POTION LOGIC
            System.out.println("Health already full!");
            return false;
        }

        potions--; //POTION LOGIC
        heal(HEAL_AMOUNT);
        System.out.println("Potion used! Potions left: " + potions);
        return true;
    }
    
    //-------------------- Health & Combat Methods ------------------//
    
    // Facing directions
    public static final int FACING_UP = 0;
    public static final int FACING_LEFT = 1;
    public static final int FACING_DOWN = 2;
    public static final int FACING_RIGHT = 3;
    private int facing = FACING_RIGHT; // default facing right

    // Attack mode: determines type of attack (case statement in GameScreen will use this)
    public static final int ATTACK_SWIPE = 0;
    private int attackMode = ATTACK_SWIPE;

    // Shielding state
    private boolean shielding = false;

    public int getCurrentHealth() { return currentHealth; }
    public int getMaxHealth() { return maxHealth; }

    public void setFacing(int f) { this.facing = f; }
    public int getFacing() { return this.facing; }

    public void setAttackMode(int mode) { this.attackMode = mode; }
    public int getAttackMode() { return this.attackMode; }

    public void setShielding(boolean s) {
        this.shielding = s;
        if (s) System.out.println("Shield ON");
        else System.out.println("Shield OFF");
    }
    public boolean isShielding() { return shielding; }

    public void takeDamage(int damage) {
        int actual = damage;
        if (shielding) {
            // While shielding, incoming damage is halved
            actual = damage / 2;
        }
        currentHealth -= actual;
        if (currentHealth < 0) currentHealth = 0;
        System.out.println("Player took " + actual + " damage. Health now: " + currentHealth + "/" + maxHealth);
    }

    public void heal(int amount) {
        currentHealth += amount;
        if (currentHealth > maxHealth) currentHealth = maxHealth;
    }

    private void loadImage(String imagePath) {
        try {
            originalImage = ImageIO.read(new File(imagePath));
            // create the scaled image once at the current size
            rescaleTo(width, height);
        } catch (IOException e) {
            System.err.println("Error loading image: " + imagePath);
            e.printStackTrace();
        }
    }

    // Rescale the loaded original image to the requested width/height and update bounds
    private void rescaleTo(int w, int h) {
        if (originalImage == null || w <= 0 || h <= 0) return;
        BufferedImage scaled = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = scaled.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(originalImage, 0, 0, w, h, null);
        g2.dispose();
        this.playerImage = scaled;
        this.width = w;
        this.height = h;
        setBounds(this.x, this.y, this.width, this.height);
    }

    // Called by Game_Screen when the tile size or panel changes
    public void setSize(int w, int h) {
        if (w <= 0 || h <= 0) return;
        rescaleTo(w, h);
    }

    // Snap the player position to the current tile grid so moves align to tiles
    public void snapToGrid(int tileW, int tileH) {
        if (tileW <= 0 || tileH <= 0) return;
        this.x = (this.x / tileW) * tileW;
        this.y = (this.y / tileH) * tileH;
    }

    public void moveStep(int panelWidth, int panelHeight) {
        if (dx != 0 || dy != 0) {
            this.x += dx;
            this.y += dy;
            dx = 0; // Stop movement after one step
            dy = 0;
        }

        // Horizontal Boundaries
        if (x < 0) x = 0;
        if (x + width > panelWidth) x = panelWidth - width;

        // Vertical Boundaries
        if (y < 0) y = 0;
        if (y + height > panelHeight) y = panelHeight - height;
    }

    // Backwards-compatible tick (delegates to moveStep so movement can be driven externally)
    public void tick(int panelWidth, int panelHeight) {
        moveStep(panelWidth, panelHeight);
    } // END tick

    public void draw(Graphics g) {
        if (playerImage != null) {
            g.drawImage(playerImage, x, y, null);
        }
        // g.fillRect(this.x, this.y, this.width, this.height); // gives the player a color
        // g.setColor(Color.GREEN); // Gives the player color
    } // END draw

    public void setDx(int dx) {
        this.dx = dx;
    } // END setDx

    public int getDx() {
        return dx;
    }

    public void setDy(int dy) {
        this.dy = dy;
    } // END setDy

    public int getDy() {
        return dy;
    }


    //=========== Adding for Mouse attacking =====================
    public void attack(){
        System.out.println("Player swings weapon!");
    }
    public void shield(){
        System.out.println("player Raises Shield!");
    }
    
} // END class