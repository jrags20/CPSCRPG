/*
  John Ragucci
  CPSC 1060: RPG Programming Assignment
  05/04/2023
*/

import java.util.Scanner;

public class RPGGame extends Throwable {

  public RPGGame(Scanner amountPlayers) throws InterruptedException {
    System.out.println("Welcome to Rags to Riches! \nHow many players are playing? \nPlease enter '1' or '2'");

    // needs to only accept 1 or 2
    while (!amountPlayers.hasNextInt()) {
      System.out.println("Please enter please enter a valid number.");
      amountPlayers.next();
    }
    int players = amountPlayers.nextInt();

    while (players != 1 && players != 2) {
      System.out.println("Please type '1' or '2'");
      
      while (!amountPlayers.hasNextInt()) {
        System.out.println("Please enter please enter a valid number.");
        amountPlayers.next();
      }
      players = amountPlayers.nextInt();
    }
    if (players == 2) {
      twoPlayer(amountPlayers);
    }
    else {
      onePlayer(amountPlayers);
    }
  }

  private class Player {
    private String playerName;
    private int pointTotal;

    public Player(String name) {
      this.playerName = name;
      this.pointTotal = 0;
    }

    public String getPlayerName() {
      return playerName;
    }

    public int getPointTotal() {
      return pointTotal;
    }

    public void setPointTotal(int newTotal) {
      pointTotal = newTotal;
    }

    public boolean decide(int dieSide, int rollAmount, int pointScored, int roll, int playingScore) {
      //double version of playTo, points, scored and point total
      double scoreAmount = (double) playingScore, pointAmount = (double) pointScored, scoreTotal = (double) pointTotal;
      //how many rolls you've taken out of the chance you will roll a 1
      double oneChance = (double) (rollAmount + 1) / (dieSide);
      //how many points you get if you rolled the most likely outcome of the two dice for every roll you had
      double likelyOutcome = ((dieSide + 1) * roll);
      //half of likelyOutcome
      double desiredOutcome = likelyOutcome / 2; 
      //The likelihood that the Bot will roll again
      //Decreases as Bot's total points increase
      double botAgain = ((desiredOutcome - pointScored) / (desiredOutcome)) * (((scoreAmount + scoreAmount / 3) - scoreTotal) / scoreAmount);
      
      if (botAgain < 0) {
        return false;
      } 
      else if (botAgain < oneChance) {
        return true;
      } 
      else {
        return false;
      } 
           
    }
  }

  public int playerTurn(Player player, Scanner turn, RPGDeck gameDeck, RPGDice gameDie) {
    int turnPoints = 0;
    RPGCards turnCard = gameDeck.drawCard();
    System.out.println(player.playerName + " drew the " + turnCard.toString());
    System.out.println(player.playerName + " has " + turnCard.getCardRank().rankValue + " rolls left");
    int rolls = turnCard.getCardRank().rankValue;

    while (rolls > 0) {
      System.out.println("Enter 'roll' to roll die, or 'end' to end your turn.");
      String input = turn.nextLine();
  
      if (input.equals("roll")) {
        int roll1 = gameDie.roll();
        int roll2 = gameDie.roll();
        int rollTotal = roll1 + roll2;
        rolls--;

        System.out.println(player.playerName + " has rolled a " + "[" + roll1 + "]" + " and a " + "[" + roll2 + "]");
        if (rollTotal == 2) {
          turnPoints = 0;
          player.setPointTotal(0);
          System.out.println("You have rolled snake-eyes. You lost all your points. \n Turn points reduced to 0. \n Total points reduced to 0. \n Turn ended.\n");
          return 0;
        }
        else if (roll1 == 1 || roll2 == 1) {
          turnPoints = 0;
          System.out.println("You rolled a 1! You lost your points in this round. \n Turn points reduced to 0. \n Turn ended.\n");
          return 0;
        }
        else {
          turnPoints += rollTotal;
          System.out.println(rollTotal + " added to turn point total. \n Turn points increased to " + turnPoints + ".\n");
        }
      }
      else if (input.equals("end")) {
        player.setPointTotal(player.pointTotal + turnPoints);
        System.out.println(turnPoints + " points scored! New point total is " + player.pointTotal);
        System.out.println("Turn has ended.");
        return turnPoints;
      }
      else {
          // keep prompting the user to enter a valid command
          while (!input.equals("roll") && !input.equals("end")) {
              System.out.println("Please enter a valid command.");
              input = turn.nextLine();
          }
          continue; // restart the loop with the new input
      }
    }

    System.out.println("Out of rolls.");
    player.setPointTotal(player.pointTotal + turnPoints);
    System.out.println(turnPoints + " points scored! " + player.playerName + "'s point total is " + player.pointTotal);
    System.out.println(" Turn ended.");
    System.out.println("");
    return turnPoints;
  }

  // computer turn for each time it is the Bot's turn
  public int computerTurn(Player player, Scanner s, RPGDeck gameDeck, RPGDice gameDie, int playingScore) throws InterruptedException {
    int turnPoints = 0;
    RPGCards turnCard = gameDeck.drawCard();
    System.out.println(player.playerName + " drew the " + turnCard.toString());
    System.out.println(player.playerName + " has " + turnCard.getCardRank().rankValue + " rolls left");
    int rolls = turnCard.getCardRank().rankValue;
    int currentRolls = 0;

    while (rolls > 0) {
      // So there can be a wait between rolls (for aesthetics)

      boolean willRoll = player.decide(gameDie.getSides(), currentRolls, turnPoints, rolls, playingScore);

      if (willRoll || currentRolls == 0) {
        int roll1 = gameDie.roll();
        int roll2 = gameDie.roll();
        int rollTotal = roll1 + roll2;
        rolls--;
        currentRolls++;
        System.out.println(player.playerName + " rolls the dice");
        System.out.println(player.playerName + " has rolled a " + "[" + roll1 + "]" + " and a " + "[" + roll2 + "]");

        if (rollTotal == 2) {
          turnPoints = 0;
          player.setPointTotal(0);
          System.out.println("Bot rolled snake-eyes. You lost your all your points. \n Turn points reduced to 0. \n Total points reduced to 0. \n Turn ended.\n");
          return 0;
        }
        else if (roll1 == 1 || roll2 == 1) {
          turnPoints = 0;
          System.out.println("Bot rolled a 1! \n Turn points reduced to 0. \n Turn ended.\n");
          return 0;
        }
        else {
          turnPoints += rollTotal;
          System.out.println(rollTotal + " added to turn point total. \n Turn points increased to " + turnPoints + ".\n");
        }
      }
      else {
        // end of Bot turn
        System.out.println("The Bot decides to hold");
        player.setPointTotal(player.pointTotal + turnPoints);
        System.out.println(turnPoints + " points scored! New point total is " + player.pointTotal);
        System.out.println(" Turn ended.");
        return turnPoints;
      }
    }

    System.out.println("Out of rolls.");
    player.setPointTotal(player.pointTotal + turnPoints);
    System.out.println(turnPoints + " points scored! " + player.playerName + "'s point total is " + player.pointTotal);
    System.out.println(" Turn has ended.");
    System.out.println("");
    return turnPoints;
  }

  public Player twoPlayer(Scanner game) {
    Player gameWin = null;
    RPGDeck gameDeck = new RPGDeck();
    gameDeck.shuffle();

    System.out.println("How many points will you play to?");
    while (!game.hasNextInt()) {
      System.out.println("Please enter valid number (ex. '100')");
      game.nextLine();
    }
    int playTo = game.nextInt();
    game.nextLine();

    System.out.println("How many sides should the game dice have?");
    while (!game.hasNextInt() || game.nextInt() < 1) {
      System.out.println("Please enter valid number of sides (ex. '6')");
      game.next();
    }
    RPGDice gameDie = new RPGDice(game.nextInt());
    
    while(gameDie.getSides() < 1) {
      System.out.println("Dice must have at least 1 side - try again");
      game.next();
      gameDie = new RPGDice(game.nextInt());
    }
    game.nextLine();

    System.out.print("Please enter Player 1 name: ");
    Player p1 = new Player(game.nextLine());
    System.out.println(p1.getPlayerName() + " entered.");

    System.out.print("Please enter Player 2 name: ");
    Player p2 = new Player(game.nextLine());
    System.out.println(p2.getPlayerName() + " entered.");

    int rounds = 0;

    while (gameWin == null) {
      rounds++;
      System.out.println("-----------------");
      System.out.println("  |  Round " + rounds + "  |");
      System.out.println("-----------------");
      System.out.println(p1.getPlayerName() + "'s Point Total: " + p1.getPointTotal());
      System.out.println(p2.getPlayerName() + "'s Point Total: " + p2.getPointTotal());
      System.out.println("----------------------------");


      playerTurn(p1, game, gameDeck, gameDie);
      if (p1.getPointTotal() >= playTo) {
        System.out.println(p1.playerName + " is the winner with a total of " + p1.getPointTotal() + " points! " + p2.getPlayerName() + " has lost!");
        gameWin = p1;
        return gameWin;
      }

      System.out.println("----------------------------");

      playerTurn(p2, game, gameDeck, gameDie);
      if (p2.getPointTotal() >= playTo) {
        System.out.println(p2.playerName + " is the winner with a total of " + p2.getPointTotal() + " points!" + p1.getPlayerName() + " has lost!");
        gameWin = p2;
        return gameWin;
      }
    }
    return gameWin;
  }

  public Player onePlayer(Scanner gameOne) throws InterruptedException {

    Player winner = null;
    RPGDeck gameDeck = new RPGDeck();
    gameDeck.shuffle();

    // A selection of comical names for the Bot
    String[] botNames = {"Evan", "Brady", "Jason", "Andrew", "Cole", "Max", "Tommy", "Will", "Mason", "Jude"};

    System.out.println("How many sides should the game dice have?");
    while (!gameOne.hasNextInt()) {
      System.out.println("Please enter valid number of sides (ex. '6')");
      gameOne.nextLine();
    }

    RPGDice gameDie = new RPGDice(gameOne.nextInt());
    gameOne.nextLine();

    System.out.println("How many points will you play to?");
    while (!gameOne.hasNextInt()) {
      System.out.println("Please enter valid number (ex. '100')");
      gameOne.nextLine();
    }

    int playTo = gameOne.nextInt();
    gameOne.nextLine();

    System.out.print("Please enter Player 1 name: ");
    Player p1 = new Player(gameOne.nextLine());
    System.out.println(p1.getPlayerName() + " entered.\n");

    int nameSelector = (int) (Math.random() * 9);

    Player p2 = new Player(botNames[nameSelector]);
    System.out.println(p2.getPlayerName() + " has entered! (Computer generated opponent)");

    int rounds = 0;

    while (winner == null) {
      rounds++;
      System.out.println("-----------------");
      System.out.println("  |  Round " + rounds + "  |");
      System.out.println("-----------------");
      System.out.println(p1.getPlayerName() + "'s Point Total: " + p1.getPointTotal());
      System.out.println(p2.getPlayerName() + "'s Point Total: " + p2.getPointTotal());
      System.out.println("----------------------------");

      playerTurn(p1, gameOne, gameDeck, gameDie);

      if (p1.getPointTotal() >= playTo) {
        System.out.println(p1.getPlayerName() + " has won the game! Congrats! You've beaten the system!");
        winner = p1;
        return winner;
      }

      System.out.println("----------------------------");

      computerTurn(p2, gameOne, gameDeck, gameDie, playTo);
      if (p2.getPointTotal() >= playTo) {
        System.out.println(p1.getPlayerName() + " got beaten by a computer! " + p2.playerName + " is the winner!");
        winner = p2;
        return winner;
      }
    }
    return winner;
  }

  public static void main(String[] args) throws InterruptedException {

    Scanner gameScan = new Scanner(System.in);

    RPGGame newGame = new RPGGame(gameScan);
  }
}