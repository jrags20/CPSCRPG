/*
  John Ragucci
  CPSC 1060: RPG Programming Assignment
  05/04/2023
*/

public class RPGDice {
  private int sides;

  public RPGDice(int sides) {
    this.sides = sides;
  }

  public int getSides() {
    return sides;
  }

  public int roll() {
    return 1 + (int) (Math.random() * sides);
  }
}