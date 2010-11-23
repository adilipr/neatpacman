package com.anji.neatpacman.ghosts;

import com.anji.integration.Activator;
import com.anji.neatpacman.GameState;
import com.anji.neatpacman.Utils;
import com.anji.tournament.Player;

public class GhostsPlayer implements Player
{
  
  Activator activator;

  public GhostsPlayer(Activator activator)
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

  public int[] move(GameState[] ghostsStates)
  {
    int[] rst = new int[ghostsStates.length];
    
    for (int i = 0; i < ghostsStates.length; ++i)
    {
      GameState gs = ghostsStates[i];
      double[] inputs = gs.getValues(1.0); // 1.0 is bias
      double[] outputs = activator.next(inputs);
      rst[i] = Utils.outputsToDirection(outputs);
    }
    
    return rst;
  }
  
}
