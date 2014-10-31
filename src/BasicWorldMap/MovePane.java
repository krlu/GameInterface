package BasicWorldMap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MovePane {

    public static void main(String[] args) {
        new MovePane();
    }

    public MovePane() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(new TestPane(null));// TODO: pass in the character/unit object!!
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public enum Direction {

        None, Up, Down, Left, Right, Blink;
    }

    @SuppressWarnings("serial")
	public class TestPane extends JPanel {
	//	private static final long serialVersionUID = -4047734207601020551L;
		private JPanel mobby;
        private Timer moveTimer;
        private Direction moveDirection = Direction.None;
        private int SPEED; 
        private int BLINK;
        private Direction currentDirection = Direction.None; 
        private double lastUsedSkillTime = -1;
        public TestPane(Object o) {
        	
        	/* creates the image on the map
        	 * can specify size and color
        	 */
            mobby = new JPanel();
            mobby.setBackground(Color.BLUE); 
            mobby.setSize(50, 50);;
            setLayout(new BorderLayout());
            JPanel pool = new JPanel(null);
            pool.add(mobby);
            add(pool);
            
            this.SPEED = 5; // will depend on the stats of the unit that's moving
            this.BLINK = 200; // depends on stats of blinking/flashing skill

            moveTimer = new Timer(20, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Container parent = mobby.getParent();
                    Rectangle bounds = mobby.getBounds();
                    
                    // can control speed at which the object moves
                    switch (moveDirection) {
                        case Up:
                            bounds.y -= SPEED;
                            currentDirection = Direction.Up;
                            break;
                        case Down:
                            bounds.y += SPEED;
                            currentDirection = Direction.Down;
                            break;
                        case Left:
                            bounds.x -= SPEED;
                            currentDirection = Direction.Left;
                            break;
                        case Right:
                            bounds.x += SPEED;
                            currentDirection = Direction.Right;
                            break;
                        case Blink:
                        	 blinkMove(bounds);
                        default:
                        	break;
                    }
                 //   SPEED = Math.max(1, SPEED*2);

                    if (bounds.x < 0) {
                        bounds.x = 0;
                    } else if (bounds.x + bounds.width > parent.getWidth()) {
                        bounds.x = parent.getWidth() - bounds.width;
                    }
                    if (bounds.y < 0) {
                        bounds.y = 0;
                    } else if (bounds.y + bounds.height > parent.getHeight()) {
                        bounds.y = parent.getHeight() - bounds.height;
                    }

                    mobby.setBounds(bounds);

                }
                
                // blinks unit a short distance across map on 3 second cooldown!
                public void blinkMove(Rectangle bounds){
                	 double now = System.currentTimeMillis();
                	 if(lastUsedSkillTime > 0 && (now - lastUsedSkillTime)/1000 < 3){
                		 System.out.println("SKILL IS ON COOLDOWN!!");
                		 return;
                	 }
                	 else{
                		 lastUsedSkillTime = now;
                		 System.out.println("BLINKED!!");
                	 }
                	 switch (currentDirection) {
                     	case Up:
                         	bounds.y -= BLINK;
                         	break;
                     	case Down:
                    	 	bounds.y += BLINK;
                         	break;
                     	case Left:
                         	bounds.x -= BLINK;
                         	break;
                     	case Right:
                         	bounds.x += BLINK;
                         	break;
                     	case Blink:
                    	 
                     	default:
                     		break;
                	 }
                }
            });
            moveTimer.setInitialDelay(0);
            InputMap im = pool.getInputMap(WHEN_IN_FOCUSED_WINDOW);
            ActionMap am = pool.getActionMap();

            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "UpPressed");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), "UpReleased");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "DownPressed");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "DownReleased");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "LeftPressed");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "LeftReleased");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "RightPressed");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "RightReleased");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0, false), "ZPressed");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0, true), "ZReleased");
            
            KeyUpAction keyUpAction = new KeyUpAction();
            am.put("UpReleased", keyUpAction);
            am.put("DownReleased", keyUpAction);
            am.put("LeftReleased", keyUpAction);
            am.put("RightReleased", keyUpAction);
            am.put("ZReleased", keyUpAction);
            
            am.put("UpPressed", new MoveAction(Direction.Up));
            am.put("DownPressed", new MoveAction(Direction.Down));
            am.put("LeftPressed", new MoveAction(Direction.Left));
            am.put("RightPressed", new MoveAction(Direction.Right));
            am.put("ZPressed", new MoveAction(Direction.Blink));

        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(400, 400); // TODO: map should not be resize-able!!!
        }

        public class KeyUpAction extends AbstractAction {

            @Override
            public void actionPerformed(ActionEvent e) {
                moveTimer.stop();
                moveDirection = Direction.None;
            }
        }

        public class MoveAction extends AbstractAction {

            private Direction direction;

            public MoveAction(Direction direction) {
                this.direction = direction;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                moveDirection = direction;
                moveTimer.start();
            }
        }
    }
}