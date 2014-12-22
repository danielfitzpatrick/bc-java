package org.bouncycastle.pqc.crypto.isogeny;

/**
 * Implementation of an elliptic curve over GF(p^2)
 * in Montgomery form: 
 * 
 *   b.y^2 = x^3 + a.x^2 + x.
 */

public class Fp2Curve {
	
	private Fp2Point infinity;
	private Fp2Field a;
	private Fp2Field b;
	private Fp2Field A24;	// (a+2)/4
	
	/**
	 * Create the elliptic curve in Montgomery form
	 * 
	 *   b.y^2 = x^3 + a.x^2 + x.
	 *   
	 *   @param a the coefficient of x^2 in GF(p^2).
	 *   @param b the coefficient of y^2 in GF(p^2).
	 */
	public Fp2Curve(Fp2Field a, Fp2Field b)
	{
		this.a   = a;
		this.b   = b;
		this.A24 = a.add(Fp2Field.SCALAR_TWO).divide(Fp2Field.SCALAR_FOUR);
		this.infinity = new Fp2Point(this,null,null,null);
	}
	
	/**
	 * Create a copy of the elliptic curve E.
	 */
	public Fp2Curve(Fp2Curve E)
	{
		this.a   = new Fp2Field(E.a);
		this.b   = new Fp2Field(E.b);
		this.A24 = this.a.add(Fp2Field.SCALAR_TWO).divide(Fp2Field.SCALAR_FOUR);
		this.infinity = new Fp2Point(this,null,null,null);
	}
	
	/**
	 * Get the coefficient of x^2 in the curve equation.
	 */
	public Fp2Field getA()
	{	
		return a;
	}
	
	/**
	 * Get the coefficient of y^2 in the curve equation.
	 */
	public Fp2Field getB()
	{
		return b;
	}
	
	/**
	 * Get the precomputed value of (a+2)/4 for the curve.
	 */
	public Fp2Field getA24()
	{
		return A24;
	}
	
	/**
	 * Get the point at infinity.
	 */
	public Fp2Point getInfinity()
	{
		return infinity;
	}

	/**
	 * Compute the j-invariant of the curve.
	 */
	 public Fp2Field getJInvariant()
	 {
		 // The j-invariant of the Montgomery curve is
		 //
		 //   j = 256 (a^2 - 3)^3 / (a^2 - 4).
		 
		 Fp2Field j, a2, a2sub3, a2sub4;
		 
		 a2 = this.a.square();
		 a2sub3 = a2.subtract(Fp2Field.SCALAR_THREE);
		 a2sub4 = a2.subtract(Fp2Field.SCALAR_FOUR);
		 
		 j = a2sub3.square().multiply(a2sub3).multiply(Fp2Field.SCALAR_TWOFIFTYSIX).divide(a2sub4);
		 
		 return j;
	 }

}
