package org.bouncycastle.pqc.crypto.isogeny;

import java.security.SecureRandom;

import org.bouncycastle.crypto.KeyGenerationParameters;

public class IsogenyKeyGenerationParameters
    extends KeyGenerationParameters
{
    private IsogenyDomainParameters params;

    public IsogenyKeyGenerationParameters(
        SecureRandom random,
        IsogenyDomainParameters params)
    {
        super(random, getStrength(params));

        this.params = params;
    }

    public IsogenyKeyGenerationParameters(
    	SecureRandom random,
    	int strength)
    {
    	super(random, strength);
    	this.params = new IsogenyDomainParameters(strength);
    }
    
    public IsogenyDomainParameters getParameters()
    {
        return params;
    }

    static int getStrength(IsogenyDomainParameters params)
    {
    	return params.E0.getA().getFieldSize()/4;
    }
}
