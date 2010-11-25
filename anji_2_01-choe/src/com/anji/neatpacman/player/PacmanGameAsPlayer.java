package com.anji.neatpacman.player;

import com.anji.neatpacman.Maze;
import com.anji.tournament.Game;
import com.anji.tournament.GameResults;
import com.anji.tournament.PlayerResults;
import com.anji.tournament.ScoringWeights;

public class PacmanGameAsPlayer implements Game
{

  @Override
  public GameResults play(PlayerResults contestantResults, PlayerResults opponentResults)
  {
    return null; // for simple tournament we don't need to return anything.
  }

  @Override
  public Class requiredPlayerClass()
  {
    return PacmanPlayer.class;
  }

  @Override
  public int getMaxScore(ScoringWeights weights)
  {
    return Maze.MAX_POSSIBLE_SCORE_FOR_PLAYER;
  }

  @Override
  public int getMinScore(ScoringWeights weights)
  {
    return Maze.MIN_POSSIBLE_SCORE_FOR_PLAYER;
  }

}
