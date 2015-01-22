package org.bouncycastle.pqc.crypto.isogeny;

import java.math.BigInteger;

public class IsogenyPrivateKeyParameters
    extends IsogenyKeyParameters
{
    private BigInteger m;
    private BigInteger n;

    public IsogenyPrivateKeyParameters(
        BigInteger m,
        BigInteger n,
        boolean isAlice,
        IsogenyDomainParameters params)
    {
        super(true, isAlice, params);
        this.m = m;
        this.n = n;
    }

    public BigInteger getM()
    {
        return m;
    }
    
    public BigInteger getN()
    {
    	return n;
    }
}
