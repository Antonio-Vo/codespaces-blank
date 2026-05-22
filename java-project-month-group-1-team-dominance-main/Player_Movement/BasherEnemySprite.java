package Player_Movement;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Point;
public class BasherEnemySprite extends Rectangle {
    private int dx = 0; // step for horizontal movement
    private int dy = 0; // step for vertical movement
    private boolean chasing = false;
    private int damage = 10; // how much damage enemy does
    
    // ======== HEALTH ======== //
    private int currentHealth = 10;
    private int maxHealth = 10;

    public int getCurrentHealth() { return currentHealth; }
    public int getMaxHealth() { return maxHealth; }
    public void takeDamage(int damageAmount) { currentHealth -= damageAmount; if (currentHealth < 0) currentHealth = 0; }

    private static final long MOVE_DELAY = 1500; // same delay as Player_Sprite
    private long lastMoveTime = 0;

    public BasherEnemySprite(int x, int y, int width, int height) {
        setBounds(x, y, width, height);
    }

    // Per-enemy tick interval (how many global ticks between moves). Basher defaults to acting every 2 ticks.
    private int tickInterval = 2;

    public void setTickInterval(int interval) { if (interval > 0) tickInterval = interval; }
    public int getTickInterval() { return tickInterval; }

    // Perform one movement step toward the player (no timing - driven externally)
    // tickCount == 0 means act immediately (backwards-compatible)
    public void moveStep(PlayerSprite player, int panelWidth, int panelHeight, long tickCount) {
        if (tickCount != 0 && (tickCount % tickInterval != 0)) return;

        // Distance from enemy to player
        int distanceX = Math.abs(player.x - this.x);
        int distanceY = Math.abs(player.y - this.y);

        int stepX = Math.max(1, player.width);
        int stepY = Math.max(1, player.height);

        // Favor moving left and right
        if (distanceX > distanceY) {
            // Move horizontally towards player
            if (player.x > this.x) dx = stepX;
            else if (player.x < this.x) dx = -stepX;
            else dx = 0;
            dy = 0;
        } else {
            // Move vertically towards player
            if (player.y > this.y) dy = stepY;
            else if (player.y < this.y) dy = -stepY;
            else dy = 0;
            dx = 0;
        }

        if (dx != 0 || dy != 0) {
            this.x += dx;
            this.y += dy;
            dx = 0;
            dy = 0;
        }

        // Bounds check
        if (this.x < 0) this.x = 0;
        if (this.x + this.width > panelWidth)
            this.x = panelWidth - this.width;
        if (this.y < 0) this.y = 0;
        if (this.y + this.height > panelHeight)
            this.y = panelHeight - this.height;
    }

    // Overload for backwards compatibility (acts immediately)
    public void moveStep(PlayerSprite player, int panelWidth, int panelHeight) {
        moveStep(player, panelWidth, panelHeight, 0);
    }

    // Backwards-compatible tick (delegates to moveStep)
    public void tick(PlayerSprite player, int panelWidth, int panelHeight) {
        moveStep(player, panelWidth, panelHeight);
    }

    // Compute intended step without applying it (used by occupancy-aware movement)
    public Point getIntendedStep(PlayerSprite player) {
        int dx = 0, dy = 0;
        int distanceX = Math.abs(player.x - this.x);
        int distanceY = Math.abs(player.y - this.y);

        int stepX = Math.max(1, player.width);
        int stepY = Math.max(1, player.height);

        if (distanceX > distanceY) {
            if (player.x > this.x) dx = stepX;
            else if (player.x < this.x) dx = -stepX;
            else dx = 0;
            dy = 0;
        } else {
            if (player.y > this.y) dy = stepY;
            else if (player.y < this.y) dy = -stepY;
            else dy = 0;
            dx = 0;
        }
        return new Point(dx, dy);
    }


        //---------------- OLD MOVEMET SYSTEM -------------- //
    //     // ACTIVATE chase mode is player close enough
    //     chasing = (distanceX < chaseDistance && distanceY < chaseDistance);

    //     if (chasing) {
    //         // Move toward player
    //         if (player.x > this.x) dx = 2;
    //         if (player.x < this.x) dx = -2;
    //     }
    //     else {
    //         // Normal patrol environment
    //         this.x += dx;

    //         // Reverse direction when hitting bounds
    //         if (this.x <= 0 || this.x + this.width >= panelWidth) {
    //             dx *= -1;
    //         }
    //     }

    //     // Apply horizontal movement
    //     this.x += dx;

    //     // Bounds check again for safety
    //     if (this.x < 0) this.x = 0;
    //     if (this.x + this.width > panelWidth)
    //         this.x = panelWidth - this.width;
    // }
    
    public boolean collidesWith(PlayerSprite player) {
        return this.intersects(player);
    }

    public int getDamage() {
        return damage;
    }

    public void draw(Graphics g) {
        g.setColor(java.awt.Color.RED);
        g.fillRect(this.x, this.y, this.width, this.height);
    }
    // Snap the enemy position to the current tile grid so moves align to tiles
    public void snapToGrid(int tileW, int tileH) {
        if (tileW <= 0 || tileH <= 0) return;
        this.x = (this.x / tileW) * tileW;
        this.y = (this.y / tileH) * tileH;
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> JaceS
