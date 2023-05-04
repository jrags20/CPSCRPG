default: RPGGame.java RPGCards.java RPGDeck.java RPGDice.java
	javac RPGGame.java RPGCards.java RPGDeck.java RPGDice.java

run: RPGGame.class RPGCards.class RPGDeck.class RPGDice.class
	java RPGGame

clean:
	rm -f *.class