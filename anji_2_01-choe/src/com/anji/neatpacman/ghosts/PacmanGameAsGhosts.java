package com.anji.neatpacman.ghosts;

import com.anji.neatpacman.Maze;
import com.anji.neatpacman.PlayGround;
import com.anji.tournament.Game;
import com.anji.tournament.GameResults;
import com.anji.tournament.PlayerResults;
import com.anji.tournament.ScoringWeights;

public class PacmanGameAsGhosts implements Game
{

  @Override
  public GameResults play(PlayerResults contestantResults, PlayerResults opponentResults)
  {
    PlayGround.get().play(opponentResults, contestantResults);
    return null; // for simple tournament we don't need to return anything.
  }

  @Override
  public Class requiredPlayerClass()
  {
    return GhostsPlayer.class;
  }

  @Override
  public int getMaxScore(ScoringWeights weights)
  {
    return Maze.MAX_POSSIBLE_SCORE_FOR_GHOSTS;
  }

  @Override
  public int getMinScore(ScoringWeights weights)
  {
    return Maze.MIN_POSSIBLE_SCORE_FOR_GHOSTS;
  }

}