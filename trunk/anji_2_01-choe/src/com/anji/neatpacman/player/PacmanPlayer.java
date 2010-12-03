package com.anji.neatpacman.player;

import com.anji.integration.Activator;
import com.anji.neatpacman.GameState;
import com.anji.neatpacman.Utils;
import com.anji.tournament.Player;

public class PacmanPlayer implements Player
{
  
  Activator activator;

  public PacmanPlayer(Activator activator)
  {
    this.activator = activator;
  }

  @Override
  public String getPlayerId()
  {
    return activator.getName();
  }

  @Override
  public void reset()
  {
    activator.reset();
  }
  
  public byte move(GameState playerState)
  {
    double[] inputs = playerState.getValues(1.0); // 1.0 is bias
    double[] outputs = activator.next(inputs);
    return Utils.outputsToDirection(outputs);
  }

}
