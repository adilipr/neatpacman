/*
 * Copyright (C) 2009 Yoonsuck Choe
 * 
 * This file is an add-on of ANJI (Another NEAT Java Implementation).
 * This add-on has the same terms and conditions as ANJI (see below).
 * This is a modified version of DoublePoleBalanceFitnessFunction.java
 * by Derek James.
 * 
 * Tue Nov  3 17:08:37 CST 2009
 *
 * --- original statement in DoublePoleBalanceFitnessFunction.java ---
 *
 * This file is a part of ANJI (Another NEAT Java Implementation).
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
 * created by Derek James on Jul 5, 2005
 */

package com.anji.navi;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.nio.DoubleBuffer;

import org.apache.log4j.Logger;
import org.jgap.BulkFitnessFunction;
import org.jgap.Chromosome;

import com.anji.imaging.IdentifyImageFitnessFunction;
import com.anji.integration.Activator;
import com.anji.integration.ActivatorTranscriber;
import com.anji.util.Arrays;
import com.anji.util.Configurable;
import com.anji.util.Properties;
import com.anji.util.Randomizer;

/**
 * This is written from scratch by Yoonsuck Choe.
 * @author Yoonsuck Choe
 */
public class XorFitnessFunction implements BulkFitnessFunction, Configurable {

private final static String ARENA_X_KEY = "navi.arena.x";
private final static String ARENA_Y_KEY = "navi.arena.y";
private final static String MAX_STEPS_KEY = "navi.maxsteps";
private final static String NUM_TRIALS_KEY = "navi.numtrials";

// Some useful physical model constants.
private static final double DUMMY = -9.8;

// private PoleBalanceDisplay display = null;

private static final double DEFAULT_ARENA_X = 40;
private double arena_x = DEFAULT_ARENA_X;

private static final double DEFAULT_ARENA_Y = 40;
private double arena_y = DEFAULT_ARENA_Y;

private final static int DEFAULT_TIMESTEPS = 10000;
private int maxTimesteps = DEFAULT_TIMESTEPS;

private final static int DEFAULT_NUM_TRIALS = 10;
private int numTrials = DEFAULT_NUM_TRIALS;

private final static Logger logger = Logger.getLogger( XorFitnessFunction.class );

private ActivatorTranscriber factory;

private Random rand;

/**
 * @see com.anji.util.Configurable#init(com.anji.util.Properties)
 */
public void init( Properties props ) throws Exception {
	try {

		// 1. Init activator transcriber
		factory = (ActivatorTranscriber) props.singletonObjectProperty( ActivatorTranscriber.class );

		// 2. pull parameters from the properties file
		arena_x = props.getDoubleProperty(ARENA_X_KEY, DEFAULT_ARENA_X);
		arena_y = props.getDoubleProperty(ARENA_Y_KEY, DEFAULT_ARENA_Y);
		maxTimesteps = props.getIntProperty(MAX_STEPS_KEY, DEFAULT_TIMESTEPS );
		numTrials = props.getIntProperty(NUM_TRIALS_KEY, DEFAULT_NUM_TRIALS );
			// * Other types: getBooleanProperty( INPUT_VELOCITY_KEY, true );

		// 3. set up randomizer  
		Randomizer randomizer = (Randomizer) props.singletonObjectProperty( Randomizer.class );
		rand = randomizer.getRand();
	}
	catch ( Exception e ) {
		throw new IllegalArgumentException( "invalid properties: " + e.getClass().toString() + ": "
				+ e.getMessage() );
	}
}

/**
 * @see org.jgap.BulkFitnessFunction#evaluate(java.util.List)
 * @see IdentifyImageFitnessFunction#evaluate(Chromosome)
 */
public void evaluate( List genotypes ) {

	// evaluate each chromosome: this is generic, so no need to touch
	Iterator it = genotypes.iterator();
	while ( it.hasNext() ) {
		Chromosome c = (Chromosome) it.next();
		evaluate( c );
	}
}

/**
 * Evaluate chromosome and set fitness.
 * @param c
 */
public void evaluate( Chromosome c ) {
	try {
		Activator activator = factory.newActivator( c );

		// calculate fitness, sum of multiple trials
		double fitness = 0;

		double[][] inputTable = {
			{-1, -1, 1}, 
			{-1, 1, 1}, 
			{1, -1, 1},
			{1, 1, 1}}; //input and bias

		double[] target = {-1, 1, 1, -1};

		double[] output;

		double[] result = new double[4];

		for ( int i = 0; i < 4; i++ ) {
			output = activator.next( inputTable[i] );
			result[i] = output[0];
			fitness += Math.abs(target[i]-output[0]);
		}
		
		// System.out.println("fitness = "+fitness+"inp="+inputTable[2][1]);

		// YC:  Fitness is the higher the better
		fitness = (8.0-fitness)*100.0/8.0;

		if (fitness >= 99.0) {
			System.out.println("Output = "+
				result[0]+" "+
				result[1]+" "+
				result[2]+" "+
				result[3]);
		}

		c.setFitnessValue((int)fitness);
	}
	catch ( Throwable e ) {
		logger.warn( "error evaluating chromosome " + c.toString(), e );
		c.setFitnessValue( 0 );
	}
}

/**
 * @see org.jgap.BulkFitnessFunction#getMaxFitnessValue()
 */
public int getMaxFitnessValue() {
	// YC: this function tells the main program what is the maximum
	// fitness attainable.
	return (99);
}

} // end of class



////////////////////////////////////////////////////////////////////////////
// scrap code
////////////////////////////////////////////////////////////////////////////


/**
 * singleTrial
 * @param activator
 *
private int singleTrial( Activator activator ) {

	int fitness = 0;

	DoubleBuffer oscillBuffer = DoubleBuffer.allocate( 10000 );

	// logger.debug( "state = " + Arrays.toString( state ) );

	// Run the pole-balancing simulation.
	int currentTimestep = 0;
	for ( currentTimestep = 0; currentTimestep < maxTimesteps; currentTimestep++ ) {
		// Network activation values
		double[] networkInput;

		// Store the accumulated state variables for cart and pole 1 within the oscillation buffer.
		oscillBuffer.put(Math.abs(state[0]) + Math.abs(state[1]) + 
								Math.abs(state[2]) + Math.abs(state[3]));
		
		// Activate the network.
		double networkOutput = activator.next( networkInput )[ 0 ];
		energyUsed += networkOutput;
		// performAction( networkOutput, state );

		if ( currentTimestep%1000==0 )
		{
			if ( currentTimestep > 99 )
			{

				if(f2>0.0)
					f2 = 0.75 / f2;
			}
			fitness += 0.1 + 0.9*f2;
		}
	}

	logger.debug( "trial took " + currentTimestep + " steps" );
	return fitness;
}
*/

