package neatpacman;

public interface Maze
{

  // defs of directions
  static final int UP    = 1;

  static final int DOWN  = 2;

  static final int LEFT  = 4;

  static final int RIGHT = 8;

  // query game state (for neural network inputs)
  GameState getPacManState();

  GameState getGhostState(int id);

  // move (and thus change game state)
  void movePacMan(int direction);

  void moveGhost(int id, int direction);

  boolean isGameOver();

  // query final scores (only used after game over, as fitness functions)
  int getPacManScore();

  int getGhostScore();

}
