package PlayerMovement;

import java.awt.Graphics;
import java.awt.Rectangle;

public class EnemySprite {

    private int x, y, width, height;
    private int dx = 2;
    private int dy = 2;
    private int damage = 1;

    private int chaseDistance = 150;
    private boolean chasing = false;

    public EnemySprite(int x, int y, int width, int height) {
        setBounds(x, y, width, height);
    }

    private void setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void tick(PlayerSprite player, int panelWidth, int panelHeight) {

        int distanceX = Math.abs(player.x - this.x);
        int distanceY = Math.abs(player.y - this.y);

        chasing = (distanceX < chaseDistance && distanceY < chaseDistance);

        if (chasing) {
            if (player.x < this.x) dx = -2;
            else if (player.x > this.x) dx = 2;

            if (player.y < this.y) dy = -2;
            else if (player.y > this.y) dy = 2;
        }

        x += dx;
        y += dy;

        // Keep enemy inside the screen
        x = Math.max(0, Math.min(x, panelWidth - width));
        y = Math.max(0, Math.min(y, panelHeight - height));
    }

    public boolean collidesWith(PlayerSprite player) {
        return getBounds().intersects(player.getBounds());
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getDamage() {
        return damage;
    }

    public void draw(Graphics g) {
        g.setColor(java.awt.Color.RED);
        g.fillRect(x, y, width, height);
    }
}
