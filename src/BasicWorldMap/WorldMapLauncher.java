package BasicWorldMap;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class WorldMapLauncher extends Applet{

	
	 public void paint (Graphics g)
	 {
	        g.drawString("Hello",40,30);
	 }

	    public static void main(String ad[])
	    {
	        JFrame jp1 = new JFrame();
	        WorldMapLauncher a = new WorldMapLauncher();
	        jp1.getContentPane().add(a, BorderLayout.CENTER);
	        jp1.setSize(new Dimension(500,500));
	        jp1.setVisible(true);
	    }
}

