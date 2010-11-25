package pacman;
import java.applet.*;
import java.net.URL;

//Loads and holds a bunch of audio files whose locations are specified
//relative to a fixed base URL.
class SoundManager
{
   // Pacman Sounds
   AudioClip      m_chompClip;
   AudioClip      m_eatGhostClip;
   AudioClip      m_pacmanDiesClip;
   AudioClip      m_returnGhostClip;
   AudioClip      m_sirenClip;
   AudioClip      m_startClip;
   AudioClip      m_ghostBlueClip;
   AudioClip      m_extraPacClip;
   AudioClip      m_eatFruitClip;
   
   static final int  SOUND_PACMANDIES_LENGTH     = 1605;
   static final int  SOUND_CHOMP_LENGTH          = 243;
   static final int  SOUND_RETURNGHOST_LENGTH    = 705;
   static final int  SOUND_START_LENGTH          = 4630;
   
   
   static final int  SOUND_CHOMP          = 1;
   static final int  SOUND_EATGHOST       = 2;
   static final int  SOUND_PACMANDIES     = 3;
   
   URL      m_baseURL;

      m_pacMan = pacMan;
      m_baseURL = baseURL;
   }

   public void loadSoundClips ()
      long beginLoadTime = System.currentTimeMillis ();
      
      m_pacmanDiesClip  = m_pacMan.getAudioClip (m_baseURL, "gs_pacmandies.au");
      m_sirenClip       = m_pacMan.getAudioClip (m_baseURL, "gs_siren_soft.au");
      m_ghostBlueClip   = m_pacMan.getAudioClip (m_baseURL, "gs_ghostblue.au");
      m_extraPacClip    = m_pacMan.getAudioClip (m_baseURL, "gs_extrapac.au");
      m_eatFruitClip    = m_pacMan.getAudioClip (m_baseURL, "gs_eatfruit.au");
      long endLoadTime = System.currentTimeMillis ();
   }

      if (m_nChompTicks > 0)
         m_nChompTicks--;
         if (m_nChompTicks == 0)
         {
            m_bChompLooping = false;
            stopSound (SOUND_CHOMP);
         }
      }
   }
   // Public method exposed for other classes to play
   // various sounds. 
      if (!m_bLoaded || !m_bEnabled)
      switch (soundEnum)
      {
      case SOUND_CHOMP:
            m_chompClip.loop ();
         break;
         m_eatGhostClip.play ();
         break;
         m_pacmanDiesClip.play ();
         break;
         m_returnGhostClip.loop ();
         break;
         if (!m_bSirenLooping)
         {
         break;
         
      case SOUND_START:
          m_startClip.play ();
          break;
          
      case SOUND_GHOSTBLUE:
          m_ghostBlueClip.loop ();
          break;
          
      case SOUND_EXTRAPAC:
          m_extraPacClip.play ();
          break;
          
      case SOUND_EATFRUIT:
          m_eatFruitClip.play ();
         
   }
   // any sound whether it is playing or looping
      switch (soundEnum)
      {
      case SOUND_CHOMP:
         m_chompClip.stop ();
         break;
         m_eatGhostClip.stop ();
         break;
         m_pacmanDiesClip.stop ();
         break;
         m_returnGhostClip.stop ();
         break;
         m_sirenClip.stop ();
         break;
         m_startClip.stop ();
         break;
         
      case SOUND_GHOSTBLUE:
         m_ghostBlueClip.stop ();
         break;
      case SOUND_EXTRAPAC:
         m_extraPacClip.stop ();
         break;
         
      case SOUND_EATFRUIT:
         m_eatFruitClip.stop ();
         break;
   }
      m_chompClip.stop ();
      m_eatGhostClip.stop ();
      m_pacmanDiesClip.stop ();
      m_returnGhostClip.stop ();
      m_sirenClip.stop ();
      m_startClip.stop ();
      m_ghostBlueClip.stop ();
      m_extraPacClip.stop ();
      m_eatFruitClip.stop ();
   }
}