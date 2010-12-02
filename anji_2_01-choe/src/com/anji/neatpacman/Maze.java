package com.anji.neatpacman;

public interface Maze
{

  // defs of directions
  static final int        ERROR                         = 0;

  static final int        UP                            = 1;

  static final int        DOWN                          = 2;

  static final int        LEFT                          = 3;

  static final int        RIGHT                         = 4;

  // query game state (for neural network inputs)
  GameState getPacManState();

  GameState getGhostState(int id);

  // tick all
  void tick();

  // move (and thus change game state)
  void setPacManDirection(int direction);

  void setGhostDirection(int id, int direction);

  boolean isGameOver();

  // query final scores (only used after game over, as fitness functions)
  int getPacManScore();

  int getGhostScore();

}
