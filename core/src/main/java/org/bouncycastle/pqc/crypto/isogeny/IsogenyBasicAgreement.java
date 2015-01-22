package org.bouncycastle.pqc.crypto.isogeny;

import java.math.BigInteger;

import org.bouncycastle.crypto.BasicAgreement;
import org.bouncycastle.crypto.CipherParameters;

/**
 * Implementation of the isogeny-based key agreement from 
 * D. Jao and L. De Feo, Towards quantum-resistant cryptosystems
 * from supersingluar elliptic curve isogenies, PQCrypto 2011. 
 */
public class IsogenyBasicAgreement
	implements BasicAgreement
{
	IsogenyPrivateKeyParameters key; 

	/**
	 * Initialise the isogeny-based key agreement.
	 * 
	 * @param key Alice or Bob's private key.
	 */
	public void init(CipherParameters key) 
	{
		this.key = (IsogenyPrivateKeyParameters) key;	
	}

	
	/**
	 * Calculate shared secret from the isogeny-based key agreement.
	 * 
	 * @param pubKey Alice or Bob's public key.
	 */
	public BigInteger calculateAgreement(CipherParameters pubKey) 
	{

		IsogenyDomainParameters domain = ((IsogenyKeyParameters) key).getParameters();

		if (((IsogenyKeyParameters) key).isAlice)
		{
			if ((((IsogenyKeyParameters) pubKey).isAlice) || 
					(((IsogenyKeyParameters) pubKey).isPrivate()))
			{
				throw new IllegalArgumentException("Need Bob's public key");	
			}
			else
			{
				Fp2Curve AliceSharedCurve = IsogenyCoreEngine.keygen(
						((IsogenyPublicKeyParameters) pubKey).getP(),
						((IsogenyPublicKeyParameters) pubKey).getQ(),
						((IsogenyPrivateKeyParameters) key).getM(),
						((IsogenyPrivateKeyParameters) key).getN(),
						domain.lA.intValue(),
						domain.StrategyA,
						null,
						null);
				
				// The j-invariant is an element of the quadratic field
				// GF(p^2) but BasicAgreements expects calculateAgreement
				// to return a BigInteger. 
				
				return AliceSharedCurve.getJInvariant().getReal();
			}
		}
		else
		{
			if (!(((IsogenyKeyParameters) pubKey).isAlice) || 
					(((IsogenyKeyParameters) pubKey).isPrivate()))
			{
				throw new IllegalArgumentException("Need Alice's public key");	
			}
			else
			{

				Fp2Curve BobSharedCurve = IsogenyCoreEngine.keygen(
						((IsogenyPublicKeyParameters) pubKey).getP(),
						((IsogenyPublicKeyParameters) pubKey).getQ(),
						((IsogenyPrivateKeyParameters) key).getM(),
						((IsogenyPrivateKeyParameters) key).getN(),
						domain.lB.intValue(),
						domain.StrategyB,
						null,
						null);
				
				// The j-invariant is an element of the quadratic field
				// GF(p^2) but BasicAgreements expects calculateAgreement
				// to return a BigInteger.
			
				return BobSharedCurve.getJInvariant().getReal();
			}
		}
	}
	
	
	/**
	 * Get the size of the prime base field.
	 */
	public int getFieldSize() {
		return key.getParameters().E0.getA().getFieldSize();
	}

}
