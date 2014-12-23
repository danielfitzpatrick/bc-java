package org.bouncycastle.pqc.crypto.isogeny;

public class IsogenyPublicKeyParameters
    extends IsogenyKeyParameters
{
    Fp2Curve E;
    Fp2Point P;
    Fp2Point Q;

    public IsogenyPublicKeyParameters(
        Fp2Curve E,
        Fp2Point P,
        Fp2Point Q,
        boolean isAlice,
        IsogenyDomainParameters params)
    {
        super(false, isAlice, params);
        this.E = E;
        this.P = P;
        this.Q = Q;
    }

    public Fp2Curve getCurve()
    {
    	return E;
    }
    
    public Fp2Point getP()
    {
        return P;
    }
    
    public Fp2Point getQ()
    {
    	return Q;
    }
}
