/*
 * Copyright (C) 2009 Yoonsuck Choe
 * 
 * This file is an add-on of ANJI (Another NEAT Java Implementation).
 * This add-on has the same terms and conditions as ANJI (see below).
 * This is a modified version of DoublePoleBalanceEvaluator.java
 * by Derek James.
 * Mon Nov 16 22:07:48 CST 2009
 *----------------------------------------------------------------------------
 * Copyright (C) 2004 Derek James and Philip Tucker
 * 
 * This file is part of ANJI (Another NEAT Java Implementation).
 * 
 * ANJI is free software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 * 
 * created by Philip Tucker on Jul 23, 2004
 */

package com.anji.navi;

import org.apache.log4j.Logger;
import org.jgap.Chromosome;
import org.jgap.Configuration;

import com.anji.persistence.Persistence;
import com.anji.util.DummyConfiguration;
import com.anji.util.Properties;
import com.anji.navi.NaviFitnessFunction;

/**
 * @author Philip Tucker
 */
public class NaviEvaluator {

private static final Logger logger = Logger.getLogger( NaviEvaluator.class );

/**
 * @param args
 * @throws Exception
 */
public static void main( String[] args ) throws Exception {

	System.out.println("args = "+args[ 0 ]);
	//System.out.println("args = "+args[ 0 ]+" "+args[ 1 ]);
	NaviFitnessFunction ff = new NaviFitnessFunction();
	Properties props = new Properties();
	props.loadFromResource( args[ 0 ] );
	ff.init( props );
	Persistence db = (Persistence) props.newObjectProperty( Persistence.PERSISTENCE_CLASS_KEY );
	Configuration config = new DummyConfiguration();
	Chromosome chrom = db.loadChromosome( args[ 1 ], config );
	if ( chrom == null )
		throw new IllegalArgumentException( "no chromosome found: " + args[ 1 ] );
	ff.enableDisplay();
	ff.evaluate( chrom );
	logger.info( "Fitness = " + chrom.getFitnessValue() );
}
}
