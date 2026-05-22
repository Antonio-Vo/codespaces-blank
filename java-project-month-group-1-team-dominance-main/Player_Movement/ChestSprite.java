
package Player_Movement;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ChestSprite {

    public int x, y, width, height;

    private BufferedImage closedImage;
    private BufferedImage[] openFrames;

    private int frameIndex = 0;
    private int frameTimer = 0;

    private boolean opening = false;
    private boolean opened = false;

    private static final int FRAME_DELAY = 8; // animation speed

    public ChestSprite(int tileCol, int tileRow, int tileW, int tileH,
                       BufferedImage closed,
                       BufferedImage[] chestOpenFrames) {

        this.width = tileW;
        this.height = tileH;
        this.x = tileCol * tileW;
        this.y = tileRow * tileH;

        this.closedImage = closed;
        this.openFrames = chestOpenFrames;
    }

    // Called every tick from GameScreen
    public void tick() {
        if (!opening) return;

        frameTimer++;
        if (frameTimer >= FRAME_DELAY) {
            frameTimer = 0;
            frameIndex++;

            if (frameIndex >= openFrames.length) {
                frameIndex = openFrames.length - 1;
                opening = false;
                opened = true;
            }
        }
    }

    public void draw(Graphics g) {
        if (opening) {
            g.drawImage(openFrames[frameIndex], x, y, width, height, null);
        }
        else if (opened) {
            g.drawImage(openFrames[openFrames.length - 1], x, y, width, height, null);
        }
        else {
            g.drawImage(closedImage, x, y, width, height, null);
        }
    }

    public void open(PlayerSprite p) {
        if (opened || opening) return;

        opening = true;
        frameIndex = 0;
        frameTimer = 0;

        p.addPotion();
        System.out.println("Chest opened! Potion +1");
    }

    public boolean isOpened() {
        return opened;
    }

    public Rectangle getHitbox() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isPlayerNearby(PlayerSprite p) {
        Rectangle nearby = new Rectangle(
            x - 10, y - 10,
            width + 20, height + 20
        );
        Rectangle playerBox = new Rectangle(p.x, p.y, p.width, p.height);
        return nearby.intersects(playerBox);
    }
}