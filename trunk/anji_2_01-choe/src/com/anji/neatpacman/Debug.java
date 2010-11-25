package com.anji.neatpacman;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class Debug
{

  private static Map<Class<?>, Boolean> _enabled = new HashMap<Class<?>, Boolean>();

  public static void setEnable(Class<?> theClass, boolean enabled)
  {
    _enabled.put(theClass, enabled);
  }
  
  private static PrintStream _out = System.err;
  
  public static void setOutputStream(PrintStream out)
  {
    _out = out;
  }

  public void debug(String fmt, Object... objs)
  {
    if (_enabled.containsKey(this.getClass()) && _enabled.get(this.getClass()))
    {
      String msg = String.format(fmt, objs);
      _out.println();
      _out.format("%s: %s", this, msg);
      _out.println();
    }
  }

}
