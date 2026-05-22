package Main;

import javax.swing.SwingUtilities;

public class Launcher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainScreen frame1 = new MainScreen();
                //frame1.setVisible(true);
            }
        });

    }

}