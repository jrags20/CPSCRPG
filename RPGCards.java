/*
  John Ragucci
  CPSC 1060: RPG Programming Assignment
  05/04/2023
*/

import java.util.HashMap;
import java.util.Map;

public class RPGCards {
  private Rank cardRank;
  private Suit cardSuit;

  
  //each Card has attributes Rank and Suit
  //the attributes for each card are permanent
  enum Suit {
    HEARTS, DIAMONDS, SPADES, CLUBS
  }
  
  enum Rank {
    ACE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), JACK(10), QUEEN(10), KING(10);
    //each rank is assigned an int value, that allows the gameplay class to utilize the card
    int rankValue;

    Rank(int rankValue) {
      this.rankValue = rankValue;
    }
  }

  private final static Map<String, RPGCards> cardCatch = initCache();

  //Card Cache map contains every possible card in a deck of 52
  //allows deck objects to create a stack of card objects
  
  private static Map<String, RPGCards> initCache() {

    Map<String, RPGCards> gameCards = new HashMap<>();

    for (Suit suit : Suit.values()) {
      for (Rank rank : Rank.values()) {
        gameCards.put(cardKey(rank, suit), new RPGCards(rank, suit));
      }
    }
    return gameCards;
  }

  static RPGCards getCard(Rank rank, Suit suit) {
    final RPGCards card = cardCatch.get(cardKey(rank, suit));
    if (card != null) {
      return card;
    }
    throw new RuntimeException("Invalid card ! " + rank + " " + suit);
  }

  public Rank getCardRank() {
    return this.cardRank;
  }

  public Suit getCardSuit() {
    return this.cardSuit;
  }

  private static String cardKey(final Rank rank, final Suit suit) {
    return rank + " of " + suit;
  }

  public RPGCards(final Rank rank, final Suit suit) {
    this.cardRank = rank;
    this.cardSuit = suit;
  }

  public String toString() {
    return this.cardRank + " of " + this.cardSuit;
  }
}