import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.HashMap;


/**
 * This class is used to create a jPanel that contains a card.
 */
public class CardFrame extends JPanel {
    private JLabel bg;
    private String[] card;
    private boolean vertical;

    public CardFrame(String[] card, boolean vertical){
        this.vertical = vertical;
        this.card = card;
        this.setOpaque(false);
        initJPanel();

    }
    public void initJPanel(){
        // sets Layout
        this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        bg = new JLabel();
        if (vertical) {
            // if vertical the vertical template is used
            bg.setIcon(new javax.swing.ImageIcon(getClass().getClassLoader().getResource("CardBG.png")));
            // adds the values to the template card
            this.add(new DrawPanel(card, vertical), new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

            this.add(bg, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
        } else {
            // if not vertical the horizontal template is used
            bg.setIcon(new javax.swing.ImageIcon(getClass().getClassLoader().getResource("CardBGhorizontal.png")));
            // adds the values to the template card
            this.add(new DrawPanel(card, vertical), new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

            this.add(bg, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
        }

    }

    /**
     * This class is used draw on the template card using Graphics2D.
     */
    public class DrawPanel extends JPanel{
        private String value;
        private String identifier;
        private String suit;
        private Color color;

        private boolean vertical;

        private HashMap<String, String> SUITS;
        public DrawPanel(String[] card, boolean vertical){
            this.vertical = vertical;
            if (vertical){
                this.setPreferredSize(new Dimension(107,170));
            } else {
                this.setPreferredSize(new Dimension(170,107));
            }

            // used to store images of suits
            SUITS = new HashMap<>();
            SUITS.put("Clubs","♣");
            SUITS.put("Diamonds","♦");
            SUITS.put("Spades","♠");
            SUITS.put("Hearts","♥");


            value = card[0];
            identifier = card[1];
            // Identifies the image on card
            if (identifier.equals("1") || identifier.equals("11") || identifier.equals("12") || identifier.equals("13")){
                switch (identifier){
                    case "1":
                        identifier = "A";
                        break;
                    case "11":
                        identifier = "J";
                        break;
                    case "12":
                        identifier = "Q";
                        break;
                    case "13":
                        identifier = "K";
                        break;
                }
            }
            // Sets color
            suit = card[2];
            if (card[3].equals("RED")){color = new Color(175, 8, 37);} else {color = Color.BLACK;};
        }
        @Override
        public void paint(Graphics g){
            Graphics2D g2 = (Graphics2D) g;
            if (vertical) {
                // darws the value vertically on the card
                g2.setColor(color);
                g2.setFont(new Font("Baskerville", Font.BOLD, 15));

                g2.drawString(identifier, 7, 25);
                g2.drawString(SUITS.get(suit), 7, 40);

                g2.setFont(new Font("Baskerville", Font.BOLD, 44));
                g2.drawString(SUITS.get(suit), 35, 95);

                // Negative sizes are used to let the letters appear upside down
                g2.setFont(new Font("Baskerville", Font.BOLD, -15));

                g2.drawString(identifier, 96, 150);
                g2.drawString(SUITS.get(suit), 98, 135);

            } else{
                // darws the value horizontally on the card
                // Affine Transform used to rotate the values
                g2.setColor(color);
                Font font = new Font("Baskerville", Font.BOLD, 15);

                AffineTransform affineTransform = new AffineTransform();
                affineTransform.rotate(Math.toRadians(270), 0, 0);
                Font rotatedFont = font.deriveFont(affineTransform);
                g2.setFont(rotatedFont);

                g2.drawString(identifier, 20, 95);
                g2.drawString(SUITS.get(suit), 35, 96);


                font = new Font("Baskerville", Font.BOLD, 15);
                affineTransform = new AffineTransform();
                affineTransform.rotate(Math.toRadians(90), 0, 0);
                rotatedFont = font.deriveFont(affineTransform);
                g2.setFont(rotatedFont);

                g2.drawString(identifier, 150, 12);
                g2.drawString(SUITS.get(suit), 135, 12);


                font = new Font("Baskerville", Font.BOLD, 44);
                affineTransform = new AffineTransform();
                affineTransform.rotate(Math.toRadians(270), 0, 0);
                rotatedFont = font.deriveFont(affineTransform);
                g2.setFont(rotatedFont);

                g2.drawString(SUITS.get(suit), 95, 70);

            }

        }
    }
}
