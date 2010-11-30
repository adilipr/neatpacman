package com.anji.neatpacman.ghosts;

import com.anji.neatpacman.Config;
import com.anji.neatpacman.Maze;
import com.anji.tournament.Game;
import com.anji.tournament.GameResults;
import com.anji.tournament.PlayerResults;
import com.anji.tournament.ScoringWeights;

public class PacmanGameAsGhosts implements Game
{

  @Override
  public GameResults play(PlayerResults contestantResults, PlayerResults opponentResults)
  {
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
    return Config.get().getGhostsMaxScore();
  }

  @Override
  public int getMinScore(ScoringWeights weights)
  {
    return Config.get().getGhostsMinScore();
  }

}
