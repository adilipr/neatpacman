package com.anji.neatpacman.ghosts;

import org.jgap.Chromosome;

import com.anji.integration.Activator;
import com.anji.integration.ActivatorTranscriber;
import com.anji.integration.TranscriberException;
import com.anji.tournament.Player;
import com.anji.tournament.PlayerTranscriber;
import com.anji.util.Configurable;
import com.anji.util.Properties;

public class GhostsPlayerTranscriber implements PlayerTranscriber, Configurable
{

  private ActivatorTranscriber activatorTranscriber;

  @Override
  public Object transcribe(Chromosome c) throws TranscriberException
  {
    return newPlayer(c);
  }

  @Override
  public Class getPhenotypeClass()
  {
    return GhostsPlayer.class;
  }

  @Override
  public Player newPlayer(Chromosome c) throws TranscriberException
  {
    Activator activator = activatorTranscriber.newActivator(c);
    return new GhostsPlayer(activator);
  }

  @Override
  public void init(Properties props) throws Exception
  {
  	activatorTranscriber = (ActivatorTranscriber) props.singletonObjectProperty( ActivatorTranscriber.class );
  }

}
