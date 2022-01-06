import org.sqlite.core.DB;

import java.util.ArrayList;

/**
 * Implementation of Player
 *
 * @author paulrecker
 */
public class Player {

    /**
     * Username of player
     */
    private String name;

    /**
     * balance/jetons
     */
    private double balance;

    /**
     * Players hand
     */
    private ArrayList<Card> hand;
    private ArrayList<Card> splitHand;

    private Card dealerFirstCard;

    /**
     * Number of Cards in Players hand
     */
    private int numCards;
    private int splitNumCards;

    /**
     * If player is logged in
     */
    private boolean loggedIn;

    /**
     * saves the current betted amount
     */
    private double currentBet;


    /**
     * Constructor of Class Player
     * @param pName
     * @param pBalance
     */
    public Player(String pName) {
        name = pName;
        balance = DBConnect.getBalance(name);
        loggedIn = true;
        DBConnect.setAchievements(name);

        // creates ArrayList and empties hand
        emptyHand();
    }


    /**
     * empties Player's hand
     */
    public void emptyHand(){
        hand = new ArrayList<>();
        splitHand = new ArrayList<>();
        numCards = 0;
        splitNumCards = 0;
    }


    /**
     * This method adds a card to the player's hand
     * @param card
     */
    public void addCardToHand(Card card){
        hand.add(card);
        numCards = hand.size();
    }


    /**
     * This method adds a card to the player's split hand
     * @param card
     */
    public void addCardToSplitHand(Card card){
        splitHand.add(card);
        splitNumCards = splitHand.size();
    }


    /**
     * This method return the sum of the player's hand
     * @return
     */
    public int getSumOfHand(){
        int sumOfHand = 0;
        for(int card = 0; card < hand.size(); card++){
            sumOfHand = sumOfHand + hand.get(card).getValue();
        }
        return sumOfHand;
    }


    /**
     * This method adds an amount to the current method
     * @param amount
     */
    public void addBalanceAmount(double amount) {
        this.balance = this.balance + amount;
        DBConnect.updateBalance(name, this.balance);
    }


    /**
     * This method places a bet
     * @param amount
     */
    public void placeBet(double amount) {
        this.balance = this.balance - amount;
        DBConnect.updateBalance(name, balance);
        currentBet = amount;
    }

    public void printHand(boolean dealer){
        if (!dealer){
            for (int card = 0; card < hand.size(); card++) {
                System.out.println(hand.get(card).toString());
            }
            System.out.println(name+" SUM: "+ getSumOfHand());
        } else {
            System.out.println(hand.get(0).toString());
            System.out.println(name+" SUM: "+ hand.get(0).getValue());
        }
    }

    // restart game
    public void restartGame(){
        emptyHand();
        currentBet = 0;
        dealerFirstCard = null;
    }





    // BEGINN SETTER AND GETTER METHODS

    public void setNumCards(int numCards) {
        this.numCards = numCards;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public double getBalance() {
        return balance;
    }

    public double getCurrentBet() {
        return currentBet;
    }

    public int getNumCards() {
        return numCards;
    }

    public ArrayList<Card> getSplitHand() {
        return splitHand;
    }

    public int getSumOfSplitHand() {
        int sumOfHand = 0;
        for(int card = 0; card < splitHand.size(); card++){
            sumOfHand = sumOfHand + splitHand.get(card).getValue();
        }
        return sumOfHand;
    }

    public Card getDealerFirstCard() {
        return dealerFirstCard;
    }

    public void setDealerFirstCard(Card dealerFirstCard) {
        this.dealerFirstCard = dealerFirstCard;
    }
}
