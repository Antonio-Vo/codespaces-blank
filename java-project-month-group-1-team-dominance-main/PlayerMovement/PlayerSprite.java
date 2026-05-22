package PlayerMovement;

import java.awt.*; 

public class PlayerSprite extends Rectangle {
    
    private int dx, dy; // Character Velocity

    public PlayerSprite(int x, int y, int width, int height, int dx, int dy){
        setBounds(x, y, width, height);
        this.dx = dx; 
        this.dy = dy;  
    }// END PlayerSprite

    public void tick(int panelWidth, int panelHeight){
        this.x += dx; 
        this.y += dy; 

        // Horizontal Boundaries
        if (x < 0) x = 0; 
        if (x + width > panelWidth) x = panelWidth - width; 

        // Vertical Boundaries 
        if (y < 0) y = 0; 
        if (y + height > panelHeight) y = panelHeight - height; 
    }// END tick

    public void draw(Graphics g){
        g.fillRect(this.x, this.y, this.width, this.height); // fills the player a color
        g.setColor(Color.GREEN); // gives the player color
    }// END draw

    public void setDx(int dx){
        this.dx = dx; 
    }// END setDx

    public void setDy(int dy){
        this.dy = dy; 
    }// END setDy
}// END CLASS