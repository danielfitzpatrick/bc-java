package org.bouncycastle.pqc.crypto.isogeny;

import java.math.BigInteger;

/** 
 * Implementation of point arithmetic on supersingular elliptic 
 * curves over GF(p^2) in Montgomery (and Edwards) form following 
 * Luca De Feo's code (https://github.com/defeo/ss-isogeny-software/).
 */
public class Fp2Point {

	private Fp2Curve curve;
	private Fp2Field x;
	private Fp2Field y;
	private Fp2Field z;

	// TODO - Make it clearer when the points being used
	// are in Montgomery or Edwards form.
	
	/** 
	 * Define a point on the elliptic curve.
	 * 
	 * @param curve elliptic curve over GF(p^2)
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param z z-coordinate
	 */
	public Fp2Point(Fp2Curve curve, Fp2Field x, Fp2Field y, Fp2Field z)
	{	
		// TODO - Check that the point lies on the given curve.
		
		this.curve = curve;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Create a copy of the point P.
	 */
	public Fp2Point(Fp2Point P)
	{
		this.curve = new Fp2Curve(P.curve);
		this.x = new Fp2Field(P.getX());
		this.y = new Fp2Field(P.getY());
		this.z = new Fp2Field(P.getZ());
	}

	/**
	 * Get the x-coordinate of the point.
	 */
	public Fp2Field getX()
	{
		return x;
	}

	/**
	 * Get the y-coordinate of the point.
	 */
	public Fp2Field getY()
	{
		return y;
	}

	/**
	 * Get the z-coordinate of the point.
	 */
	public Fp2Field getZ()
	{
		return z;
	}

	/**	
	 * Get the curve on which the point lies.
	 */
	public Fp2Curve getCurve()
	{
		return curve;
	}

	/**
	 * Check whether it is the point at infinity.
	 */
	public boolean isInfinity()
	{
		return (x == null) && (y == null);
	}

	
	// TODO - Most of the code below is an almost line by line 
	// translation into Java of De Feo's C code.  It could be
	// simplified by checking which intermediate values are 
	// actually being reused.  The readability could also be 
	// improved by some restructuring.

	/**	
	 * Montgomery point doubling
	 */
	public Fp2Point twice()
	{
		if (this.isInfinity())
			return this;
		
		if ((this.y!=null) && (this.y.isZero()))
			return this.curve.getInfinity();

		if (z==null)
			throw new RuntimeException("Affine doubling not implemented");
		
		if (x.isEqual(Fp2Field.ZERO))
			return this.curve.getInfinity();
	

		// Montgomery point doubling
		Fp2Field a  = x.add(z);	
		Fp2Field aa = a.square();
		Fp2Field b  = x.subtract(z);
		Fp2Field bb = b.square();
		Fp2Field c  = aa.subtract(bb);

		Fp2Field X = aa.multiply(bb);
		Fp2Field Z = curve.getA24().multiply(c).add(bb).multiply(c);


		if (Z.isZero())
			return this.curve.getInfinity();
		else
			return new Fp2Point(curve,X,null,Z);	

	}
	
	
	/**
	 * Montgomery point tripling.
	 */
	public Fp2Point triple()
	{
		if (this.isInfinity())
			return this;
		
		if(x.isZero())
			return this.curve.getInfinity();
		
		// Double and store some of the intermediate values.
		Fp2Field a      = x.add(z);
		Fp2Field aa     = a.square();
		Fp2Field b      = x.subtract(z);
		Fp2Field bb     = b.square();
		Fp2Field c      = aa.subtract(bb);
		Fp2Field x2     = aa.multiply(bb);
		Fp2Field z2     = curve.getA24().multiply(c).add(bb).multiply(c);

		Fp2Field x2pz2  = x2.add(z2);
		Fp2Field x2sz2  = x2.subtract(z2);
		Fp2Field da     = x2sz2.multiply(a);
		Fp2Field cb     = x2pz2.multiply(b);
		
		Fp2Field dapcb  = da.add(cb);
		Fp2Field dapcb2 = dapcb.square();
		Fp2Field X      = dapcb2.multiply(z);

		Fp2Field damcb  = da.subtract(cb);
		Fp2Field damcb2 = damcb.square();
		Fp2Field Z      = damcb2.multiply(x);

		if(Z.isZero())
			return this.curve.getInfinity();
		else
			return new Fp2Point(curve,X,null,Z);	

	}

	// Three-point Montgomery ladder addition step:
	//   P1 = 2 P1
	//   P2 = DoubleAndAdd(P1, P2, D2)
	//   P3 = DoubleAndAdd(P1, P3, D3)
	void mont_tradd(Fp2Point P1, Fp2Point P2, Fp2Point P3, Fp2Point d2, Fp2Point d3)
	{	
		Fp2Field a1 = P1.x.add(P1.z);
		Fp2Field b1 = P1.x.subtract(P1.z);
		Fp2Field a2 = P2.x.add(P2.z);
		Fp2Field b2 = P2.x.subtract(P2.z);
		Fp2Field a3 = P3.x.add(P3.z);
		Fp2Field b3 = P3.x.subtract(P3.z);

		// P3
		Fp2Field da     = b3.multiply(a1);
		Fp2Field cb     = a3.multiply(b1);
		Fp2Field dapcb  = da.add(cb);
		Fp2Field dapcb2 = dapcb.square();
		Fp2Field x3     = dapcb2.multiply(d3.z);

		Fp2Field damcb  = da.subtract(cb);
		Fp2Field damcbs = damcb.square();
		Fp2Field z3     = damcbs.multiply(d3.x);

		P3.x = x3; 
		P3.z = z3;

		// P2
		da          = b2.multiply(a1);
		cb          = a2.multiply(b1);	
		dapcb       = da.add(cb);
		dapcb2      = dapcb.square();
		Fp2Field x2 = dapcb2.multiply(d2.z);

		damcb       = da.subtract(cb);
		damcbs      = damcb.square();
		Fp2Field z2 = damcbs.multiply(d2.x);

		P2.x = x2; 
		P2.z = z2;

		// P1
		Fp2Field aa      = a1.square();
		Fp2Field bb      = b1.square();	
		Fp2Field e       = aa.subtract(bb);
		Fp2Field A24e    = P1.curve.getA24().multiply(e);
		Fp2Field A24epbb = A24e.add(bb);
		Fp2Field z1      = e.multiply(A24epbb);
		Fp2Field x1      = aa.multiply(bb);

		P1.x = x1;
		P1.z = z1;
	}

	//	3 point Montgomery ladder to compute P+[t]Q
	public Fp2Point mont_3ladder(BigInteger t,Fp2Point P,Fp2Point Q, Fp2Point QP)
	{
		Fp2Point A = new Fp2Point(P.curve, Fp2Field.ONE, null, Fp2Field.ZERO);
		Fp2Point B = new Fp2Point(Q.getCurve(), Q.getX(), null, Q.getZ());
		Fp2Point C = new Fp2Point(P.getCurve(), P.getX(), null, P.getZ());

		int bit=t.bitLength()-1;

		for(;bit>=0;bit--)
		{
			if(t.testBit(bit)==false)
				mont_tradd(A,B,C,Q,P);
			else
				mont_tradd(B,A,C,Q,QP);
		}

		return C;
	}

	/**
	 * Converts a Montgomery point to an Edwards point.
	 */
	public Fp2Point mont_to_ed()
	{
		Fp2Field XPZ  = x.add(z);
		Fp2Field XMZ  = x.subtract(z); 	
		Fp2Field YXPZ = y.multiply(XPZ);
		Fp2Field YXMZ = y.multiply(XMZ);
		Fp2Field XXPZ = x.multiply(XPZ);

		// The Edwards point x(x+z)/y(x+z) , y(x-z)/y(x+z). 
		return new Fp2Point(curve, XXPZ.divide(YXPZ), YXMZ.divide(YXPZ), null);
	}

	
	public Fp2Point shamir(Fp2Point P,Fp2Point Q,BigInteger m,BigInteger n)
	{	
		// Convert curve to Edwards form 
		Fp2Field Am = P.curve.getA();
		Fp2Field Bm = P.curve.getB();
		Fp2Field a  = (Am.add(Fp2Field.SCALAR_TWO)).divide(Bm);
		Fp2Field d  = (Am.subtract(Fp2Field.SCALAR_TWO)).divide(Bm);

		// Convert points to affine Edwards form
		Fp2Point aP = P.mont_to_ed();
		Fp2Point aQ = Q.mont_to_ed();

		// Compute P+Q using affine Edwards
		Fp2Field C    = aP.x.multiply(aQ.x);
		Fp2Field D    = aP.y.multiply(aQ.y);
		Fp2Field A    = aP.x.add(aP.y);
		Fp2Field B    = aQ.x.add(aQ.y);
		Fp2Field CD   = C.multiply(D);
		Fp2Field E    = d.multiply(CD);
		Fp2Field E2   = E.square();
		Fp2Field OME2 = Fp2Field.ONE.subtract(E2);
		Fp2Field OPE  = E.add(Fp2Field.SCALAR_ONE);
		Fp2Field aC   = a.multiply(C);
		Fp2Field DmaC = D.subtract(aC);
		Fp2Field PQy  = OPE.multiply(DmaC).divide(OME2);

		Fp2Field OME    = Fp2Field.TWO.subtract(OPE);
		Fp2Field ABmCmD = A.multiply(B).subtract(C).subtract(D);
		Fp2Field PQx    = OME.multiply(ABmCmD).divide(OME2);

		int bit = m.bitLength();
		if (n.bitLength()>bit)
			bit=n.bitLength();

		Fp2Field F, H, J;
		Fp2Point R = new Fp2Point(curve, Fp2Field.ZERO, Fp2Field.ONE, Fp2Field.ONE);
		
		for ( ; bit >=0 ; bit--)
		{
			// Double using projective Edwards
			B   = (R.x.add(R.y)).square();
			C   = R.x.square();
			D   = R.y.square();
			E   = a.multiply(C);
			F   = E.add(D);
			H   = R.z.square();
			J   = F.subtract(H.multiply(Fp2Field.SCALAR_TWO));
			R.x = J.multiply((B.subtract(C)).subtract(D));
			R.y = F.multiply(E.subtract(D));
			R.z = F.multiply(J);

			// Double and Add using projective Edwards
			int r = 0;
			if (m.testBit(bit))
				r = 1; 
			if (n.testBit(bit))
				r = r+2; 

			if (r!=0)
			{
				if (r==1)
				{
					C = R.x.multiply(aP.x);
					D = R.y.multiply(aP.y);
					H = aP.x.add(aP.y);
				}
				else if (r==2)
				{
					C = R.x.multiply(aQ.x);
					D = R.y.multiply(aQ.y);
					H = aQ.x.add(aQ.y);
				}
				else
				{
					C = R.x.multiply(PQx);
					D = R.y.multiply(PQy);
					H = PQx.add(PQy);
				}
				
				Fp2Field G, HmRxPRy;
				
				B       = R.z.square();
				E       = d.multiply(C.multiply(D));
				F       = B.subtract(E);
				G       = B.add(E);
				HmRxPRy = (R.x.add(R.y)).multiply(H);
				R.x     = R.z.multiply(F).multiply((HmRxPRy.subtract(C)).subtract(D));
				R.y     = R.z.multiply(G).multiply(D.subtract(a.multiply(C)));
				R.z     = F.multiply(G);

			}

		}

		// Convert back from Edwards to Montgomery
		Fp2Field RzPRy = R.z.add(R.y);
		Fp2Field RzMRy = R.z.subtract(R.y);

		R.y = RzPRy.multiply(R.z);
		R.z = RzMRy.multiply(R.x);
		R.x = RzPRy.multiply(R.x);

		return R;

	}

}



