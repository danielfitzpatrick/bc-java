package org.bouncycastle.pqc.crypto.isogeny;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;

import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.security.SecureRandom;

public class IsogenyKeyPairGenerator
	implements AsymmetricCipherKeyPairGenerator
{
	private IsogenyKeyGenerationParameters param;

	public void init(KeyGenerationParameters param)
	{
		this.param = (IsogenyKeyGenerationParameters) param;
	}

	public AsymmetricCipherKeyPair generateKeyPair(boolean isAlice)
	{
		IsogenyDomainParameters domain = param.getParameters();
		SecureRandom rnd = param.getRandom();

		if (isAlice)
		{

			BigInteger lAeA = domain.lA.pow(domain.eA);

			BigInteger n = new BigInteger(lAeA.bitLength(), rnd).mod(lAeA);
			BigInteger m = new BigInteger(lAeA.bitLength(), rnd).mod(lAeA);

			while ((n.mod(domain.lA).signum() == 0) && (m.mod(domain.lA).signum() == 0))
			{
				n = new BigInteger(lAeA.bitLength(), param.getRandom()).mod(lAeA);
				m = new BigInteger(lAeA.bitLength(), param.getRandom()).mod(lAeA);
			}

			Fp2Curve EA = IsogenyCoreEngine.keygen(
					domain.PA,
					domain.QA, 
					m, 
					n, 
					domain.lA.intValue(), 
					domain.StrategyA, 
					domain.PB, 
					domain.QB);

			Fp2Point PhiPB = IsogenyCoreEngine.getPhiP();
			Fp2Point PhiQB = IsogenyCoreEngine.getPhiQ();

			return new AsymmetricCipherKeyPair(
					new IsogenyPublicKeyParameters(EA, PhiPB, PhiQB, true, domain),
					new IsogenyPrivateKeyParameters(m, n, true, domain));

		}
		else
		{
			BigInteger lBeB = domain.lA.pow(domain.eA);

			BigInteger n = new BigInteger(lBeB.bitLength(), rnd).mod(lBeB);
			BigInteger m = new BigInteger(lBeB.bitLength(), rnd).mod(lBeB);

			while ((n.mod(domain.lB).signum() == 0) && (m.mod(domain.lB).signum() == 0))
			{
				n = new BigInteger(lBeB.bitLength(), param.getRandom()).mod(lBeB);
				m = new BigInteger(lBeB.bitLength(), param.getRandom()).mod(lBeB);
			}
	
			Fp2Curve EB = IsogenyCoreEngine.keygen(
					domain.PB,
					domain.QB, 
					m, 
					n, 
					domain.lB.intValue(), 
					domain.StrategyB, 
					domain.PA, 
					domain.QA);

			Fp2Point PhiPA = IsogenyCoreEngine.getPhiP();
			Fp2Point PhiQA = IsogenyCoreEngine.getPhiQ();

			return new AsymmetricCipherKeyPair(
					new IsogenyPublicKeyParameters(EB, PhiPA, PhiQA, false, domain),
					new IsogenyPrivateKeyParameters(m, n, false, domain));

		}

	}


	//  Don't use this as we need to specify whether we
	//  are generating keys for Alice or Bob.
	public AsymmetricCipherKeyPair generateKeyPair() 
	{
		throw new InvalidParameterException("Need to specify if generating keys for Alice or Bob.");
	}
}
