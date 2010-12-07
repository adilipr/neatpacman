package com.anji.neatpacman.player;

import pacman.Thing;

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
  
  public byte[] move(GameState playerState)
  {
    double[] inputs = playerState.getValues(1.0); // 1.0 is bias
    double[] outputs = activator.next(inputs);
    
    byte[] dirs = new byte[] { Thing.UP, Thing.DOWN, Thing.LEFT, Thing.RIGHT };
    
    for (int i = 0; i < outputs.length - 1; ++i)
    {
      for (int j = i+1; j < outputs.length; ++j)
      {
        if (outputs[i] > outputs[j])
        {
          double tmp = outputs[i];
          outputs[i] = outputs[j];
          outputs[j] = tmp;
          
          byte tmp1 = dirs[i];
          dirs[i] = dirs[j];
          dirs[j] = tmp1;
        }
      }
    }
    
    return dirs;
  }

}
