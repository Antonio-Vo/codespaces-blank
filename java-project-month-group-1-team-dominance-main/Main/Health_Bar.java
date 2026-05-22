package Main;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import Player_Movement.PlayerSprite;

public class Health_Bar extends JPanel {

    private int spriteWidth = 200;   // SET SIZE
    private int spriteHeight = 40;

    private PlayerSprite player;

    public Health_Bar(PlayerSprite player) {
        this.player = player;
        setPreferredSize(new Dimension(spriteWidth, spriteHeight));
        setOpaque(false);
    }

    public class HealthSpriteLoader {

    public static final ArrayList<Image> healthFrames = new ArrayList<>();

    static {
        loadSprites();
        // System.out.println("Loaded health sprites: " + HealthSpriteLoader.healthFrames.size());  // Test for all health sprites
    }

    private static void loadSprites() {
        for (int i = 0; i <= 10; i++) {
<<<<<<< HEAD
            String path = "/resources/HealthBarSprites/HealthBar(" + (100 - i * 10) + "%).png";
=======
            String path = "/resources/HealthBar(" + (100 - i * 10) + "%).png";
>>>>>>> JaceS

            java.net.URL imgUrl = HealthSpriteLoader.class.getResource(path);
            if (imgUrl == null) {
                System.err.println("Warning: Failed to load: " + path);
                continue; // skip this sprite
            }

            Image img = new ImageIcon(imgUrl).getImage();
            healthFrames.add(img);
        }
    }
}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int currentHealth = player.getCurrentHealth();
        int maxHealth = player.getMaxHealth();

        // Convert health to percentage
        double healthRatio = (double) currentHealth / maxHealth;

        // Total sprites (assumes sprite 0 = full, last = empty)
        int maxIndex = HealthSpriteLoader.healthFrames.size() - 1;

        // Calculate correct sprite index
        int spriteIndex = maxIndex - (int) Math.round(healthRatio * maxIndex);

        // Safety clamp
        spriteIndex = Math.max(0, Math.min(spriteIndex, maxIndex));

        // Draw the sprite
        Image healthSprite = HealthSpriteLoader.healthFrames.get(spriteIndex);
        g.drawImage(healthSprite, 0, 0, getWidth(), getHeight(), null);
    }

    public void updateHealthBar() {
        repaint();
    }
}