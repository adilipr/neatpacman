package pacman;
import java.awt.*;
import java.util.*;

class TopCanvas extends Canvas
	Font        m_font;
   Graphics    m_offGraphics;

	public TopCanvas (GameModel gameModel, int width, int height)
      super ();
		m_gameModel = gameModel;
	}

	public void update(Graphics g)
		paint(g);
	}

	public void paint(Graphics g)
		Dimension   dim = getSize ();
      
      // Create double buffer if it does not exist or is not
      // the right size
      if (m_offImage == null ||
          m_offDim.width != dim.width ||
          m_offDim.height != dim.height)
      {
         m_offDim = dim;
         m_offImage = createImage (m_offDim.width, m_offDim.height);
         m_offGraphics = m_offImage.getGraphics ();
      }
      
	   m_offGraphics.fillRect (0, 0, m_offDim.width, m_offDim.height);

		m_offGraphics.setColor (Color.white);

		m_offGraphics.setFont (m_font);
		FontMetrics fm = m_offGraphics.getFontMetrics ();

		y = 20 + fm.getAscent() + fm.getDescent();
		m_offGraphics.drawString ("HIGH SCORE", x, y);

      // SCORE

      g.drawImage(m_offImage, 0, 0, this);
	}
}

class BottomCanvas extends Canvas
	Font        m_font;
   PacMan      m_pacMan;
   Graphics    m_offGraphics;

	public BottomCanvas (PacMan pacMan, GameModel gameModel, int width, int height)
      super ();
		m_gameModel = gameModel;
      m_pacMan = pacMan;
	}

	public void update(Graphics g)
		paint(g);
	}

	public void paint(Graphics g)
      Dimension dim = getSize ();
      
      // Create double buffer if it does not exist or is not
      // the right size
      if (m_offImage == null ||
          m_offDim.width != dim.width ||
          m_offDim.height != dim.height)
      {
         m_offDim = dim;
         m_offImage = createImage (m_offDim.width, m_offDim.height);
         m_offGraphics = m_offImage.getGraphics ();
      }
      
      String stageString = "Level " + Integer.toString (m_gameModel.m_stage);
	   m_offGraphics.fillRect (0, 0, m_offDim.width, m_offDim.height);

      m_offGraphics.setFont(m_font);
		FontMetrics fm = m_offGraphics.getFontMetrics();

      y = fm.getAscent() + fm.getDescent();
		m_offGraphics.drawString (stageString, x, y);
      y += fm.getAscent() + fm.getDescent();
		for (int i = 0; i < m_gameModel.m_nLives; i++)
         m_offGraphics.fillArc (x, y, (int)pacManDiameter, (int)pacManDiameter, -45, -200);
         x += pacManDiameter * 1.5;  
      }
      
      y += 2 * (int)pacManDiameter + fm.getAscent() + fm.getDescent();
      x = 0;
      
      y += fm.getAscent() + fm.getDescent();
      m_offGraphics.drawString ("\'N\' for New Game", x, y);
      
      y += fm.getAscent() + fm.getDescent();
      m_offGraphics.drawString ("\'P\' to Pause", x, y);
      
      y += fm.getAscent() + fm.getDescent();
      m_offGraphics.drawString ("\'A\' for About", x, y);
      
      y += fm.getAscent() + fm.getDescent();
//         m_offGraphics.drawString ("\'S\' for No Sound", x, y);
//      else
//         m_offGraphics.drawString ("\'S\' for Sound", x, y);
      //y += fm.getAscent() + fm.getDescent();
      //m_offGraphics.drawString ("\'I\'  for Insane AI", x, y);
      
		g.drawImage (m_offImage, 0, 0, this);
	}
}