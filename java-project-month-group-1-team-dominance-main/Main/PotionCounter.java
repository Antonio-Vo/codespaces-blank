package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import Player_Movement.PlayerSprite;

public class PotionCounter extends JComponent {

    private PlayerSprite player;
    private BufferedImage[] sprites;

    public PotionCounter(PlayerSprite player, BufferedImage[] sprites) {
        this.player = player;
        this.sprites = sprites;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int potionCount = Math.min(player.getPotions(), 15);
        g.drawImage(sprites[potionCount], 0, 0, getWidth(), getHeight(), null);
    }

    public void refresh() {
        repaint();
    }
}