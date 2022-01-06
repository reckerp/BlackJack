/**
 * This class saves the History of one game
 */
public class GameHist {
    private String gameId;
    private String bet;
    private String winner;
    private String gains;
    private String dealerHand;
    private String playerHand;

    public GameHist(String gameId, String bet, String winner, String gains, String dealerHand, String playerHand) {
        this.gameId = gameId;
        this.bet = bet;
        this.winner = winner;
        this.gains = gains;
        this.dealerHand = dealerHand;
        this.playerHand = playerHand;
    }


    //GETTER

    public String getBet() {
        return bet;
    }

    public String getWinner() {
        return winner;
    }

    public String getGains() {
        return gains;
    }

    public String getDealerHand() {
        return dealerHand;
    }

    public String getPlayerHand() {
        return playerHand;
    }

    public String getGameId() {
        return gameId;
    }

    // SETTER

    public void setBet(String bet) {
        this.bet = bet;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}
