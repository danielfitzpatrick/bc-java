package org.bouncycastle.pqc.crypto.isogeny;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.util.test.SimpleTest;

/**
 * Testing the isogeny-based key agreeement from D. Jao and 
 * L. De Feo, Towards quantum-resistant cryptosystems from 
 * supersingluar elliptic curve isogenies, PQCrypto 2011. 
 */
public class IsogenyTest 
    extends SimpleTest
{

    public String getName()
    {
        return "IsogenyKeyExchange";
    }

    public void checkParams(int security)
        throws Exception
    {	
    	IsogenyKeyGenerationParameters param = 
    			new IsogenyKeyGenerationParameters(new SecureRandom(), security);
     
    	IsogenyKeyPairGenerator gen = new IsogenyKeyPairGenerator();
    	gen.init(param);
    	
    	// Generating Alice's keys
    	AsymmetricCipherKeyPair AliceKeys = gen.generateKeyPair(true);
    	
    	// Generating Bob's keys
    	AsymmetricCipherKeyPair BobKeys = gen.generateKeyPair(false);
    	
    	// Computing Alice's shared secret
    	IsogenyBasicAgreement AliceAgree = new IsogenyBasicAgreement();
    	AliceAgree.init(AliceKeys.getPrivate());    	
    	BigInteger AliceJ = AliceAgree.calculateAgreement(BobKeys.getPublic());
    	
       	// Computing Bob's shared secret
    	IsogenyBasicAgreement BobAgree = new IsogenyBasicAgreement();   
    	BobAgree.init(BobKeys.getPrivate());    	
    	BigInteger BobJ = BobAgree.calculateAgreement(AliceKeys.getPublic());
    	
    	if (!(AliceJ.equals(BobJ)))
    		fail("Shared secrets are not equal.");
    
    }
    
    public void performTest()
            throws Exception
        {
            checkParams(10);
            checkParams(64);
            checkParams(128);
            checkParams(192);
            checkParams(256);
        }
    
    public static void main(String[] args)
        {
            runTest(new IsogenyTest());
        }
    
}
