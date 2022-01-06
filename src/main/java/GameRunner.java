import javax.swing.*;
import java.util.ArrayList;

/**
 * Implementation of the Game Runner (MODEL)
 *
 * @author paulrecker
 */

// TODO: Winning message is not shown
public class GameRunner {
    /**
     * - create Player, Dealer Obj
     * - Create deck
     * - Start playing
     */

    private int gameID = 0;
    private final View view;
    private final Player p1;
    private final Player dealer;
    private final ArrayList<GameHist> gameHistory;
    private String winner;
    private Deck deck;
    private double currentBet;
    private boolean playedDoubleDown = false;
    private boolean splitGame = false;
    private int currentHand = 1;
    private boolean firstMove = false; // Because game needs to be initiated
    private boolean gameOver = false;
    private boolean standhand1 = false;
    private boolean standhand2 = false;


    public GameRunner(View pView) {
        view = pView;
        String user = view.getUserName();

        p1 = new Player(user);
        dealer = new Player("Dealer");

        deck = new Deck(2, true);
        gameHistory = new ArrayList<>();
        initiateGame();
    }

    /**
     * Initiates game
     */
    public void initiateGame() {
        gameID++;
        p1.emptyHand();
        dealer.emptyHand();
        currentBet = 0;
        firstMove = true;  // Player can begin playing

    }


    public void dealCards(){
        hitAction();
        dealerHit(true);
        hitAction();
        dealerHit(false);
    }





    /**
     * Transfers sum of hand to view
     * @param showDealer
     */
    public void transferHandSum(boolean showDealer) {
        String playerSum = switch (currentHand) {
            case 1 -> Integer.toString(p1.getSumOfHand());
            case 2 -> Integer.toString(p1.getSumOfSplitHand());
            default -> "0";
        };
        String dealerSum;
        if (showDealer) {
            dealerSum = Integer.toString(dealer.getSumOfHand());
        } else {
            try {
                dealerSum = Integer.toString(dealer.getDealerFirstCard().getValue());
            } catch (NullPointerException e) {
                dealerSum = "0";
            }
        }


        view.displayHands(playerSum, dealerSum);
    }

    /**
     * Lets player hit
     */
    public void hitAction() {
        int handNr = currentHand;
        if (firstMove) {
            if (currentBet != 0) {
                firstMove = false;
            } else {
                view.askforBet();
            }
        }
        if (handNr == 1 && !isBust(p1, 1) && !firstMove && !gameOver) {
            Card dealtCard = deck.dealCard();

            if (dealtCard.getIdentifier() == 1 && p1.getSumOfHand() > 10) {
                dealtCard.setValue(1);
            }
            p1.addCardToHand(dealtCard);
            view.addCardPlayer(dealtCard.toArray());
            if (isBust(p1, 1)) {
                standAction();
            }
        } else if (handNr == 2 && !isBust(p1, 2) && !gameOver) {
            Card dealtCard = deck.dealCard();

            if (dealtCard.getIdentifier() == 1 && p1.getSumOfHand() > 10) {
                dealtCard.setValue(1);
            }
            p1.addCardToSplitHand(dealtCard);
            view.addCardPlayerSplit(dealtCard.toArray());
            if (isBust(p1, 2)) {
                standAction();
            }
        }
        transferHandSum(false);

    }

    /**
     * Lets dealer hit
     * @param firstCard
     */
    public void dealerHit(boolean firstCard) {
        Card dealtCard = deck.dealCard();
        dealer.addCardToHand(dealtCard);
        if (firstCard) {
            dealer.setDealerFirstCard(dealtCard);
            view.addCardDealer(dealtCard.toArray());
        } else {
            view.addBlindCard();
        }
    }

    /**
     * Player stands and dealer hit until he has min. 17 sum of hand
     */
    public void standAction() {
        if (firstMove) {
            if (currentBet != 0) {
                firstMove = false;
            } else {
                view.askforBet();
            }
        }
        if (!firstMove) {
            if (splitGame && currentHand == 1) {
                standhand1 = true;
                currentHand = 2;
            }
            else {
                if (splitGame && currentHand == 2) {
                    standhand2 = true;
                }
                while (dealer.getSumOfHand() < 17) {
                    Card dealtCard = deck.dealCard();
                    dealer.addCardToHand(dealtCard);
                    view.addBlindCard();
                }
                transferHandSum(true);
                standhand1 = true;
                gameOver();
            }
        }
    }

    /**
     * Lets player play double
     */
    public void doubleAction() {
        if (firstMove) {
            if (currentBet != 0) {
                firstMove = false;
            } else {
                view.askforBet();
            }
        }
        if (!firstMove) {
            playedDoubleDown = true;
            placeBet(currentBet);
            currentBet = currentBet * 2;
            view.updateBalance();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Card dealtCard = deck.dealCard();
            p1.addCardToHand(dealtCard);
            view.addCardPlayerDouble(dealtCard.toArray());
            transferHandSum(false);
            standAction();
        }
    }

    /**
     * Lets Player play split
     */
    public void splitAction() {
        if (firstMove) {
            if (currentBet != 0) {
                firstMove = false;
            } else {
                view.askforBet();
            }
        }
        if (!firstMove && p1.getHand().get(0).getIdentifier() == p1.getHand().get(1).getIdentifier() && p1.getHand().size() == 2) { // Only split when two first are a pair and there are two cards
            splitGame = true;
            placeBet(currentBet);
            currentBet = currentBet * 2;
            view.updateBalance();
            view.playerJPanelRemoveAll();

            ArrayList<Card> clipHand = p1.getHand();
            int clipHandSum = clipHand.size();
            p1.emptyHand();

            for (int i = 0; i < clipHandSum / 2; i++) {
                p1.addCardToHand(clipHand.get(i));
                view.addCardPlayer(clipHand.get(i).toArray());
            }
            transferHandSum(false);

            for (int i = clipHandSum / 2; i < clipHandSum; i++) {
                p1.addCardToSplitHand(clipHand.get(i));
                view.addCardPlayerSplit(clipHand.get(i).toArray());
            }
        } else {
            view.jOptionPane("You can't split this hand");
        }
    }


    /**
     * Returns true if persons hand is bust
     * @param p
     * @param hand
     * @return
     */
    private boolean isBust(Player p, int hand) {
        if (hand == 1) {
            if (p.getSumOfHand() > 21) {
                gameOver();
                return true;
            }
        } else if (hand == 2) {
            if (p.getSumOfSplitHand() > 21) {
                gameOver();
                return true;
            }
        }
        return false;
    }

    /**
     * Checks hand for blackjack or bust
     * @param handNr
     * @return
     */
    public int checkHands(int handNr) {
        if (handNr == 1) {
            if (p1.getSumOfHand() == 21 && !(dealer.getSumOfHand() == 21)) {
                return 1; // player bj
            } else if (p1.getSumOfHand() > 21 && !(dealer.getSumOfHand() > 21)) {
                return 2; // player bust
            } else if (dealer.getSumOfHand() > 21 && !(p1.getSumOfHand() > 21)) {
                return 1; // dealer bust
            } else if (dealer.getSumOfHand() == 21 && !(p1.getSumOfHand() == 21)){
                return 2; // dealer bj
            }else {
                return 0;
            }
        } else if (handNr == 2) {
            if (p1.getSumOfSplitHand() == 21 && !(dealer.getSumOfHand() == 21)) {
                return 1;
            } else if (p1.getSumOfSplitHand() > 21 && !(dealer.getSumOfHand() > 21)) {
                return 2;
            } else if (dealer.getSumOfHand() > 21 && !(p1.getSumOfSplitHand() > 21)) {
                return 1;
            } else if (dealer.getSumOfHand() == 21 && !(p1.getSumOfSplitHand() == 21)){
                return 2;
            }else {
                return 0;
            }
        }

        return 0;
    }

    /**
     * Determines winner
     * @param handNr
     * @return
     */
    public int determineWinner(int handNr) {
        if (handNr == 1) {
            if (p1.getSumOfHand() > dealer.getSumOfHand()) {
                return 1;
            } else if (p1.getSumOfHand() < dealer.getSumOfHand()) {
                return 2;
            } else {
                return 0;
            }
        } else if (handNr == 2) {
            if (p1.getSumOfSplitHand() > dealer.getSumOfHand()) {
                return 1;
            } else if (p1.getSumOfSplitHand() < dealer.getSumOfHand()) {
                return 2;
            } else {
                return 0;
            }
        }

        return 0;
    }

    /**
     * If the Game is over this method evaluates the winner
     */
    public void gameOver(){
        int playerWin = 0;
        int dealerWin = 0;
        // 0 = draw, 1 = player, 2 = dealer
        if((checkHands(1) != 0 || standhand1)) {
            if (checkHands(1) != 0) { // if bust or blackjack
                if (checkHands(1) == 1) { // dealer is bust or player has bj
                    playerWin++;
                } else if (checkHands(1) == 2) { // player is bust or dealer has bj
                    dealerWin++;
                }
            }

            if (standhand1 && checkHands(1) == 0) { // stand, no bj or bust
                if (determineWinner(1) == 1) { // player lower 22 but higher than dealer
                    playerWin++;
                } else if (determineWinner(1) == 2) { // dealer lower 22 but higher than player
                    dealerWin++;
                }
            }

            if (splitGame && (checkHands(2) != 0 || standhand2) && !playingStatusSplitHand()) {
                if (checkHands(2) != 0) {
                    if (checkHands(2) == 1) {
                        playerWin++;
                    } else if (checkHands(2) == 2) {
                        dealerWin++;
                    }
                }

                if (standhand2 && checkHands(2) == 0) {
                    if (determineWinner(2) == 1) {
                        playerWin++;
                    } else if (determineWinner(2) == 2) {
                        dealerWin++;
                    }
                }
            }

            // Evaluate winner
            if (!splitGame) {
                if (playerWin > dealerWin) {
                    view.announceWinner(p1.getName());
                    winner = p1.getName();
                    view.changeButtonsActivation(false);
                    setBalance(currentBet * 2);

                } else if (playerWin < dealerWin) {
                    view.announceWinner(dealer.getName());
                    winner = dealer.getName();
                    view.changeButtonsActivation(false);
                } else if (playerWin ==0 && dealerWin == 0) {
                    view.announceWinner("Draw");
                    winner = "Draw";
                    view.changeButtonsActivation(false);
                    setBalance(currentBet);

                }
            } else if (splitGame && (p1.getSumOfSplitHand() > 21 || standhand2)){
                if (playerWin > dealerWin) {
                    view.announceWinner(p1.getName());
                    winner = p1.getName();
                    view.changeButtonsActivation(false);
                    setBalance(currentBet * 2);

                } else if (playerWin < dealerWin) {
                    view.announceWinner(dealer.getName());
                    winner = dealer.getName();
                    view.changeButtonsActivation(false);

                } else if (playerWin == 0 && dealerWin == 0) {
                    view.announceWinner("Draw");
                    winner = "Draw";
                    view.changeButtonsActivation(false);
                    setBalance(currentBet);

                }
                restartGame();
            }
            if (!splitGame){
                restartGame();
            }
        }


    }

    /**
     * This method returns true if split hand is playable
     * @return
     */
    public boolean playingStatusSplitHand(){
        if (splitGame) {
            if(p1.getSumOfHand() > 21 || p1.getSumOfHand() == 21 || standhand2){
                currentHand = 2;
                return false;
            } else{
                return true;
            }
        } else {
            return false;
        }
    }


    /**
     * Returns data of player
     * @param data
     * @return
     */
    public String getPlayerData(String data){
        switch (data){
            case "balance":
                return String.valueOf(p1.getBalance());
            case "name":
                return p1.getName();
        }
        return "";
    }

    /**
     * Lets player place bet
     * @param pBet
     */
    public void placeBet(double pBet){
        currentBet = pBet;
        p1.placeBet(currentBet);
    }

    public void firstBet(double pBet){
        currentBet = pBet;
        dealCards();
        p1.placeBet(currentBet);
    }

    /**
     * Sets balance and updates view
     * @param amount
     */
    public void setBalance(double amount){
        p1.addBalanceAmount(amount);
        view.updateBalance();
    }



    /**
     * Restarts the game by using the restart game methods of all other classes
     * Resets everything to default
     */
    public void restartGame(){
        transferHandSum(true);

        String gains = "";
        if (winner.equals("Draw")){
            gains = "0";
        } else if (winner.equals(p1.getName())){
            gains = String.valueOf(currentBet * 2);
        } else if (winner.equals(dealer.getName())){
            gains = String.valueOf(-currentBet);
        }

        String p1hand;
        if(splitGame){
            p1hand = p1.getSumOfHand() + " / " + p1.getSumOfSplitHand();
        } else {
            p1hand = String.valueOf(p1.getSumOfHand());
        }

        gameHistory.add(new GameHist(String.valueOf(gameID), String.valueOf(currentBet), winner, gains, String.valueOf(dealer.getSumOfHand()), p1hand));


        winner = "";
        JOptionPane.showMessageDialog(null, "New Round");
        p1.restartGame();
        dealer.restartGame();

        // Clear View
        view.restartGame();

        // restart GameRunner vars
        deck = new Deck(2, true);
        currentBet = 0;
        currentHand = 1;
        standhand1 = false;
        standhand2 = false;
        playedDoubleDown = false;
        splitGame = false;
        gameOver = false;

        initiateGame();


        //restart View
        view.updateBalance();
        transferHandSum(false);
        view.changeButtonsActivation(true);

    }

    /**
     * Claim Daily Bonus
     */
    public void dailyBonus(){
        if (DBConnect.isDailyBValid(p1.getName())){
            int multiplier = DBConnect.getAchievementMultiplier(p1.getName());
            p1.addBalanceAmount(multiplier*1000);
            view.updateBalance();
            DBConnect.setDailyBDate(p1.getName());
        } else{
            JOptionPane.showMessageDialog(null, "You have already claimed your daily bonus!");
        }
    }

    /**
     * Returns the Game History of one session as an ArrayList
     * @return
     */
    public ArrayList<GameHist> getGameHistory() {
        return gameHistory;
    }
}
