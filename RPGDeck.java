/*
  John Ragucci
  CPSC 1060: RPG Programming Assignment
  05/04/2023
*/

import java.util.ArrayList;
import java.util.Collections;

public class RPGDeck {

    private final ArrayList<RPGCards> deckCards;

    public RPGDeck() {
        this.deckCards = initializeDeck();
    }

    private ArrayList<RPGCards> initializeDeck() {
        ArrayList<RPGCards> deckCards = new ArrayList<>();
        for (RPGCards.Suit suit : RPGCards.Suit.values()) {
            for (RPGCards.Rank rank : RPGCards.Rank.values()) {
                deckCards.add(RPGCards.getCard(rank, suit));
            }
        }
        return deckCards;
    }

    public String toString() {
        return "" + deckCards;
    }

    public RPGCards drawCard() {
        RPGCards pulledCard = deckCards.get(0);
        deckCards.remove(0);
        return pulledCard;
    }

    public void shuffle() {
        Collections.shuffle(deckCards);
    }
}