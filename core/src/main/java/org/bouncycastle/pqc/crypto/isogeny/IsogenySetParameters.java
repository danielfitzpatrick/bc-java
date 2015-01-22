package org.bouncycastle.pqc.crypto.isogeny;

import java.math.BigInteger;

/**
 * Toy parameters for testing
 */
public class IsogenySetParameters {
		
	public IsogenySetParameters()
	{	
		// Nothing to do
	}



	public static IsogenyDomainParameters getParameters(int strength)
	{
		//  Set the prime p = 2^eA*2^eB*f - 1
		
		int eA, eB, f;
		
		if(strength <= 10)
		{
			eA = 22;  // Alice's exponent
			eB = 15;  // Bob's exponent
			f  = 1;   // Cofactor			
		}
		else
			throw new RuntimeException("Strength "+strength+" not implemented");

		BigInteger p = (BigInteger.valueOf(2).pow(eA)).
				multiply(BigInteger.valueOf(3).pow(eB)).
				multiply(BigInteger.valueOf(f)).
				subtract(BigInteger.ONE);



		// For simplicity we will use the supersingular curve 
		// over GF(p^2)
		//
		//    E0: y^2 = x^3 + x.

		Fp2Field.init(p);
		Fp2Curve E0 = new Fp2Curve(Fp2Field.ZERO, Fp2Field.ONE);



		// Optimal strategies for computing the isogenies.

		int[] StrategyA, StrategyB;
		
		if(strength <= 10)
		{
			// Alice's strategy
			int[] sA = {0,0,1,2,2,3,3,4,5,5,5,6,7,8,8,8,8,9,11,11,12};
			StrategyA = new int[sA.length];
			for(int i=0; i<sA.length; i++) 
				StrategyA[i] = sA[i];

			// Bob's strategy
			int[] sB = {0,0,1,2,2,3,3,4,5,5,5,6,7,8,8,8 }; 
			StrategyB = new int[sB.length];
			for(int i=0; i<sB.length; i++) 
				StrategyB[i] = sB[i];
		}
		else
			throw new RuntimeException("Strength "+strength+" not implemented");


		// Bases for the 2^eA- and 3^eB-torsion subgroups

		BigInteger PAx_re,PAx_im,PAy_re,PAy_im;
		BigInteger QAx_re,QAx_im,QAy_re,QAy_im;
		BigInteger PBx_re,PBx_im,PBy_re,PBy_im;
		BigInteger QBx_re,QBx_im,QBy_re,QBy_im;
		
		if (strength <= 10)
		{
			// Alice's first basis point
			PAx_re = new BigInteger("31612513142002");
			PAx_im = new BigInteger("35933776374688");
			PAy_re = new BigInteger("19246407781407");
			PAy_im = new BigInteger("49074146622181");

			// Alice's second basis point
			QAx_re = new BigInteger("48009746044732");
			QAx_im = new BigInteger("4876092162382");
			QAy_re = new BigInteger("26169020297700");
			QAy_im = new BigInteger("56689060188274");

			// Bob's first basis point
			PBx_re = new BigInteger("16795381774846");
			PBx_im = new BigInteger("33180004795105");
			PBy_re = new BigInteger("19434723997089");
			PBy_im = new BigInteger("33443291101753");

			// Bob's second basis point
			QBx_re = new BigInteger("5304855958389");
			QBx_im = new BigInteger("57670207364709");
			QBy_re = new BigInteger("11271641788085");
			QBy_im = new BigInteger("10254065864740");
		}
		else
			throw new RuntimeException("Strength "+strength+" not implemented");


		// Alice's basis for E0[2^eA]

		Fp2Point PA = new Fp2Point(E0,
				new Fp2Field(PAx_re,PAx_im),
				new Fp2Field(PAy_re,PAy_im),
				Fp2Field.ONE);

		Fp2Point QA = new Fp2Point(E0,
				new Fp2Field(QAx_re,QAx_im),
				new Fp2Field(QAy_re,QAy_im),
				Fp2Field.ONE);

		// Bob's basis for E0[3^eB]

		Fp2Point PB = new Fp2Point(E0,
				new Fp2Field(PBx_re,PBx_im),
				new Fp2Field(PBy_re,PBy_im),
				Fp2Field.ONE);

		Fp2Point QB = new Fp2Point(E0,
				new Fp2Field(QBx_re,QBx_im),
				new Fp2Field(QBy_re,QBy_im),
				Fp2Field.ONE);	


		return new IsogenyDomainParameters(E0,
				eA,PA,QA,StrategyA,
				eB,PB,QB,StrategyB);

	}
}
