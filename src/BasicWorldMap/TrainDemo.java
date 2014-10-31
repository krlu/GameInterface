package BasicWorldMap;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class TrainDemo {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Train Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null);
        frame.add(new TrainCanvas());
        frame.setVisible(true);
    }

}

@SuppressWarnings("serial")
class TrainCanvas extends JComponent {

    private int lastX = 0;
    private int lastY = 0;
    
    public static final int SPEED = 3;
    
    public TrainCanvas() {
        Thread animationThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    repaint();
                    setDelay(10);
                }
            }          
            private void setDelay(int milliseconds){
            	 try {Thread.sleep(milliseconds);} catch (Exception ex) {}
            }
        });

        animationThread.start();
    }

    public void paintComponent(Graphics g, int i) {
        Graphics2D gg = (Graphics2D) g;

        int w = getWidth();
        int h = getHeight();

        int trainW = 100;
        int trainH = 10;
        int trainSpeed = SPEED;

        int x = lastX + trainSpeed;
        int y = lastY + trainSpeed;
        
        if (x > w + trainW) {
            x = -trainW;    
        }
        if (y > h + trainH) {
            y = -trainH;    
        }
   
        gg.setColor(Color.BLACK);
        gg.fillRect(x, y, trainW, trainH);

        lastX = x;
        lastY = y;
    }

}