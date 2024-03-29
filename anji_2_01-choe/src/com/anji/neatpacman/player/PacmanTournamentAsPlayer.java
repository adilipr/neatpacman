package com.anji.neatpacman.player;

import java.util.List;

import com.anji.neatpacman.PlayGround;
import com.anji.tournament.PlayerResults;
import com.anji.tournament.SimpleTournament;

public class PacmanTournamentAsPlayer extends SimpleTournament
{

  private int numOpponents;
  
  private PlayGround playGround;

  @Override
  public int getMaxScore()
  {
    return getGame().getMaxScore(null) * numOpponents;
  }

  @Override
  public int getMinScore()
  {
    return getGame().getMinScore(null) * numOpponents;
  }

  @Override
  protected void startTournament()
  {
    List<PlayerResults> playerResults = getResults();
    playGround = PlayGround.get();
    playGround.registerPlayers(playerResults);
  }

  @Override
  protected void endTournament()
  {
    numOpponents = playGround.getGhosts().size();
    playGround.waitForDone("player");
  }

  @Override
  protected PlayerPair nextPlayerPair()
  {
    // should not be called
    return null;
  }

  @Override
  protected boolean hasNextPlayerPair()
  {
    return false;
  }

}
