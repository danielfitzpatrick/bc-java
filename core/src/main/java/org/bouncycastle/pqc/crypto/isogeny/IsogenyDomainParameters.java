package org.bouncycastle.pqc.crypto.isogeny;

import java.math.BigInteger;

/**
 * Domain parameters for the isogeny-based key agreement from 
 * D. Jao and L. De Feo, Towards quantum-resistant cryptosystems 
 * from supersingular elliptic curve isogenies, PQCrypto 2011.
 */
public class IsogenyDomainParameters
{
	// Note that we are restricting the key agreement to the case 
	// where lA = 2 and lB = 3.
	
	public Fp2Curve E0;       // Supersingular curve over GF(p^2)
	
	public BigInteger lA = BigInteger.valueOf(2);
	public int eA;            // Alice's exponent
	public Fp2Point PA, QA;   // Alice's basis for the torsion subgroup E0[2^{e_A}]
	public int[] StrategyA;   // Alice's strategy for computing 2^eA-isogenies
	
	public BigInteger lB = BigInteger.valueOf(3);
	public int eB;            // Bob's exponent
	public Fp2Point PB, QB;   // Bob's basis for the torsion subgroup E0[3^{e_B}]
	public int[] StrategyB;   // Bob's strategy for computing 3^eB-isogenies
	
	
	/**
	 * Set the domain parameters for the elliptic curve
	 * isogeny-based key exchange.
	 * 
	 * @param E0 the supersingular curve
	 * @param PA Alice's first basis point
	 * @param QA Alice's second basis point
	 * @param PB Bob's first basis point
	 * @param QB Bob's second basis point
	 * @param StrategyA Alice's strategy for computing isogenies
	 * @param StrategyB Bob's strategy for computing isogenies
	 */
	public IsogenyDomainParameters(
			Fp2Curve E0, 
			int eA,
			Fp2Point PA,
			Fp2Point QA,
			int[] StrategyA,
			int eB,
			Fp2Point PB,
			Fp2Point QB,
			int[] StrategyB)
	{
		this.E0 = E0;
		
		this.eA = eA;
		this.PA = PA;
		this.QA = QA;
		this.StrategyA = StrategyA;
		
		this.eB = eB;
		this.PB = PB;
		this.QB = QB;
		this.StrategyB = StrategyB;
	}
	
	
	/**
	 * Set the domain parameters for the elliptic curve
	 * isogeny-based key exchange.
	 *  
	 * @param strength the (classical) security level.
	 */
	public IsogenyDomainParameters(int strength)
	{
		IsogenyDomainParameters param = IsogenySetParameters.getParameters(strength);
		
		this.E0 = param.E0;
		
		this.eA = param.eA;
		this.PA = param.PA;
		this.QA = param.QA;
		this.StrategyA = param.StrategyA;
		
		this.eB = param.eB;
		this.PB = param.PB;
		this.QB = param.QB;
		this.StrategyB = param.StrategyB;
		
	}
	
}
