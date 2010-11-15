package neatpacman;

public interface Maze
{

  static final int UP    = 1;

  static final int DOWN  = 2;

  static final int LEFT  = 4;

  static final int RIGHT = 8;

  void movePacMan(int direction);

  void moveGhost(int id, int direction);

  boolean isGameOver();

  int getPacManScore();

  int getGhostScore();

}
