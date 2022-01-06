/**
 * Implementation of the class Card
 *
 * @author paulrecker
 */
public class Card {

    /**
     * The value resembles the value of the card
     */
    private int value;

    /**
     * The identifier identifies the Card: Ace = 1; Jack-King = 11-13
     */
    private int identifier;

    /**
     * One of the four valid suits
     */
    private Suit suit;

    /**
     * Color of Card: Red or Black
     */
    private CardColor cardColor;

    /**
     * Construcotr of the class Card
     * @param pValue Value of the Card
     * @param pIdentifier Identifier of the Card: Ace = 1; Jack-King = 11-13
     * @param pSuit Suit of the Card
     * @param pCardColor Color of the Card
     */
    public Card(int pValue, int pIdentifier, Suit pSuit, CardColor pCardColor){
        value = pValue;
        identifier = pIdentifier;
        suit = pSuit;
        cardColor = pCardColor;
    }

    /**
     * returns value of Card
     * @return
     */
    public int getValue() {
        return value;
    }

    /**
     * returns identifier
     * @return
     */
    public int getIdentifier() {
        return identifier;
    }

    /**
     * returns suit
     * @return
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * returns card color
     * @return
     */
    public CardColor getCardColor() {
        return cardColor;
    }

    @Override
    public String toString() {
        return "Card{" +
                "value=" + value +
                ", identifier=" + identifier +
                ", suit=" + suit +
                ", cardColor=" + cardColor +
                '}';
    }

    /**
     * returns the data of a card as an array
     * @return String[]
     */
    public String[] toArray(){
        return new String[]{String.valueOf(value), String.valueOf(identifier), suit.toString(), cardColor.toString()};
    }

    /**
     * sets value of Card
     * @param value of card
     */
    public void setValue(int value) {
        this.value = value;
    }
}
