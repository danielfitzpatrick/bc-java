package org.bouncycastle.pqc.crypto.isogeny;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

public class IsogenyKeyParameters
    extends AsymmetricKeyParameter
{
    IsogenyDomainParameters params;
    boolean isAlice;
    
    protected IsogenyKeyParameters(
        boolean isPrivate,
        boolean isAlice,
        IsogenyDomainParameters params)
    {
        super(isPrivate);

        this.isAlice = isAlice;
        this.params = params;
    }

    public IsogenyDomainParameters getParameters()
    {
        return params;
    }
}
