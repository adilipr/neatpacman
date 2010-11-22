package com.anji.neatpacman.player;

import java.util.List;

import com.anji.neatpacman.MyPlayerPair;
import com.anji.neatpacman.PlayGround;
import com.anji.tournament.PlayerResults;
import com.anji.tournament.SimpleTournament;

public class PacmanTournamentAsPlayer extends SimpleTournament
{
  
  private int i;
  
  private int numOpponents;
  
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
    PlayGround.get().registerPlayers(playerResults);
    i = 0;
  }

  @Override
  protected void endTournament()
  {
    numOpponents = PlayGround.get().getGhosts().size();
    PlayGround.get().waitForFinish();
  }

  @Override
  protected PlayerPair nextPlayerPair()
  {
    MyPlayerPair mpp = PlayGround.get().getPlayerPairs().get(i);
    PlayerPair pp = new PlayerPair();
    pp.contestant = mpp.contestant;
    pp.opponent = mpp.opponent;
    i++;
    return pp;
  }

  @Override
  protected boolean hasNextPlayerPair()
  {
    return PlayGround.get().getPlayerPairs().size() > i;
  }

}
