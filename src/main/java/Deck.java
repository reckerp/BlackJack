import java.util.ArrayList;
import java.util.Random;


/**
 * Implementation of Deck
 *
 * @author paulrecker
 */
public class Deck {
    /**
     * Array of all cards in deck
     */
    private ArrayList<Card> cardArray;

    /**
     * Card Stack
     */
    private Stack<Card> cardStack;

    /**
     * How many cards are in the deck
     */
    private int numCards;

    /**
     * If deck should be shuffled
     */
    private boolean shuffle;

    /**
     * Constructor of Class Deck
     * @param pNumDecks number of cards in deck
     * @param pShuffle if deck should be shuffled
     */
    public Deck(int pNumDecks, boolean pShuffle){
        cardArray = new ArrayList<>();
        cardStack = new Stack<>();
        numCards = pNumDecks * 52;
        shuffle = pShuffle;

        // New deck is created as an ArrayList
        for (int deck = 0; deck < pNumDecks; deck++) {
            for (int suit = 0; suit < 4; suit++) {
                for (int identifier = 1; identifier <= 13; identifier++) {
                    int clip;
                    if (identifier > 10) { // determine the value of Card
                        clip = 10;
                    } else if (identifier == 1) { // ace
                        clip = 11;
                    } else {
                        clip = identifier; // number
                    }

                    int color = 0;
                    if (suit == 1 || suit == 3) { // determine the color of the card
                        color = 0;
                    } else if (suit == 2 || suit == 0){
                        color = 1;
                    }
                    cardArray.add(new Card(clip ,identifier, Suit.values()[suit], CardColor.values()[color])); // creation of new card
                }
            }
        }

        if (shuffle){
            shuffle();
        }
        ALtoStack();
    }


    /**
     * Shuffles the ArrayList/Deck
     */
    public void shuffle() {
        Random rand = new Random();
        Card clip;
        int swap;

        for (int i = 0; i < 5; i++) { // shuffles 5 times
            for (int current = 0; current < numCards; current++) { // exchanges every position of every card
                swap = rand.nextInt(numCards);

                clip = cardArray.get(current);
                cardArray.set(current, cardArray.get(swap));
                cardArray.set(swap, clip);

            }
        }
    }

    /**
     * Deals out the next card on top
     * @return Card on top
     */
    public Card dealCard(){
        Card topCard = cardStack.top();
        cardStack.pop();
        return topCard;
    }


    /**
     * Prints out deck
     * Test method
     */
    public void printDeck(){
        for (int i = 0; i < cardArray.size(); i++) {
            System.out.println(cardArray.get(i).toString());
        }
    }

    /**
     * Private method:
     * ArrayList to Stack
     */
    private void ALtoStack(){
        cardStack = new Stack<>();
        for (int card = 0; card < cardArray.size(); card++) {
            cardStack.push(cardArray.get(card));
        }
    }

}
