package com.anji.neatpacman;

public interface Maze
{

  // TODO
  public static final int MAX_POSSIBLE_SCORE_FOR_PLAYER = 0;

  public static final int MIN_POSSIBLE_SCORE_FOR_PLAYER = 0;

  public static final int MAX_POSSIBLE_SCORE_FOR_GHOSTS = -MIN_POSSIBLE_SCORE_FOR_PLAYER;

  public static final int MIN_POSSIBLE_SCORE_FOR_GHOSTS = -MAX_POSSIBLE_SCORE_FOR_PLAYER;

  // defs of directions
  static final int        UP                            = 1;

  static final int        DOWN                          = 2;

  static final int        LEFT                          = 4;

  static final int        RIGHT                         = 8;

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
