package org.bouncycastle.pqc.crypto.isogeny;

import java.math.BigInteger;


/** 
 * Implementation of the quadratic extension field GF(p^2) 
 * for p = 3 (mod 4).  Field elements are represented as 
 * z = a + i.b where a,b lie in GF(p) and i^2 = -1.
 */ 
public class Fp2Field
{
	private static BigInteger p = null;
	
	private BigInteger re;	// Real component of the field element.
	private BigInteger im;  // Imaginary component of the field element.

	
	// Useful field constants
	
	public static Fp2Field ZERO;
	public static Fp2Field ONE;
	public static Fp2Field TWO;
	public static Fp2Field THREE;
	public static Fp2Field FOUR;	
	
	
	// Useful scalar constants for performance.
	//
	// Warning: they are not considered elements of the
	// quadratic field so cannot be used in some situations.
	
	public static BigInteger SCALAR_ONE                  = BigInteger.valueOf(1);
	public static BigInteger SCALAR_TWO                  = BigInteger.valueOf(2);
	public static BigInteger SCALAR_THREE                = BigInteger.valueOf(3);
	public static BigInteger SCALAR_FOUR                 = BigInteger.valueOf(4);
	public static BigInteger SCALAR_SIX                  = BigInteger.valueOf(6);
	public static BigInteger SCALAR_TWOFIFTYSIX          = BigInteger.valueOf(256);

	
	/**
	 * Initialise the quadratic field GF(p^2).
	 * 
	 * @param prime a prime that is 3 (mod 4)
	 */
	public static void init(BigInteger prime)
	{
		if(!prime.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3)))
			throw new IllegalArgumentException("Prime needs to be 3 mod 4");
		
		p = prime;
		
		ZERO 				 = new Fp2Field(0);
		ONE 			     = new Fp2Field(1);
		TWO					 = new Fp2Field(2);
		THREE				 = new Fp2Field(3);
		FOUR				 = new Fp2Field(4);	 
	}

	
	// Field element creation
	
	/**
	 * Create the field element z = a + i.b in GF(p^2).
	 * 
	 * @param a the real component of z.
	 * @param b the imaginary component of z.
	 */
	public Fp2Field(int a, int b)
	{
		if (p == null)
			throw new IllegalArgumentException("Field not initialised");
		
		this.re = BigInteger.valueOf(a);
		this.im = BigInteger.valueOf(b);
	}    


	/**
	 * Create the field element z = a + i.0 in GF(p^2).
	 * 
	 * @param a the real component of z.
	 */
	public Fp2Field(int a)
	{
		this(a, 0);
	}
	
	
	/**
	 * Create the field element z = a + i.b in GF(p^2).
	 * 
	 * @param a the real component of z.
	 * @param b the imaginary component of z.
	 */
	public Fp2Field(BigInteger a, BigInteger b)
	{
		if (p == null)
			throw new IllegalArgumentException("Field not initialised");

		if(a.compareTo(p)>=0)
		{
			throw new IllegalArgumentException("Real component too large in field element");
		}
		this.re = a;

		if(b.compareTo(p)>=0)
		{
			throw new IllegalArgumentException("Imaginary component too large in field element");
		}
		
		this.im = b;
	}
	
	/**
	 * Create a copy of the field element z.
	 * 
	 * @param z an element of GF(p^2).
	 */
	public Fp2Field(Fp2Field z)
	{
		this.re = z.re;
		this.im = z.im;
	}

	
	// GF(p^2) field arithmetic
	
	/**
	 * Add an element of GF(p^2).
	 */
	public Fp2Field add(Fp2Field w)
	{
		return new Fp2Field(re.add(w.re).mod(p), im.add(w.im).mod(p));
	}
	
	/**
	 * Add a scalar from GF(p).
	 */
	public Fp2Field add(BigInteger a)
	{
		return new Fp2Field(re.add(a).mod(p), im);
	}
	
	/**
	 * Subtract an element of GF(p^2).
	 */
	public Fp2Field subtract(Fp2Field w)
	{   
		return new Fp2Field(re.subtract(w.re).mod(p), im.subtract(w.im).mod(p));  
	}
	
	/**
	 * Subtract a scalar from GF(p).
	 */
	public Fp2Field subtract(BigInteger a)
	{
		return new Fp2Field(re.subtract(a).mod(p), im);
	}
	
	/**
	 * Multiply by an element of GF(p^2).
	 */
	public Fp2Field multiply(Fp2Field w)
	{ 
		return new Fp2Field( 
				re.multiply(w.re).subtract(im.multiply(w.im)).mod(p),
				re.multiply(w.im).add(im.multiply(w.re)).mod(p));
	}
	
	/**
	 * Multiply by a scalar from GF(p).
	 */
	public Fp2Field multiply(BigInteger a)
	{
		return new Fp2Field(re.multiply(a).mod(p), im.multiply(a).mod(p));
	}
	
	/**
	 * Divide by an element of GF(p^2).
	 */
	public Fp2Field divide(Fp2Field w)
	{
		return this.multiply(w.invert());
	}
	
	/**
	 * Divide by a scalar from GF(p).
	 */
	public Fp2Field divide(BigInteger a)
	{
		BigInteger modinv = a.modInverse(p);
		
		return new Fp2Field(re.multiply(modinv).mod(p),im.multiply(modinv).mod(p));
	}
	
	/**
	 * Negate the element of GF(p^2).
	 */
	public Fp2Field negate()
	{
		return new Fp2Field(p.subtract(re).mod(p), p.subtract(im).mod(p));
	}
	
	/**
	 * Square the element of GF(p^2).
	 */
	public Fp2Field square()
	{
		BigInteger temp = re.multiply(im);
		
		return new Fp2Field( 
				re.multiply(re).subtract(im.multiply(im)).mod(p),
				temp.add(temp).mod(p));        
	}
	
	/**
	 * Invert the element of GF(p^2).
	 */
	public Fp2Field invert()
	{
		BigInteger norminv = re.multiply(re).add(im.multiply(im)).modInverse(p);

		return new Fp2Field( 
				re.multiply(norminv).mod(p),
				p.subtract(im.multiply(norminv)).mod(p));
	}

	
	// Miscellaneous
	
	/**
	 * Check whether the field element is zero.
	 */
	public boolean isZero()
	{
		return ((re.signum()==0) && (im.signum()==0));
	}

	/**
	 * Get the real component of the field element.
	 */
	public BigInteger getReal()
	{
		return re;
	}
	
	/**
	 * Get the imaginary component of the field element.
	 */
	public BigInteger getImaginary()
	{
		return im;
	}
	
	/**
	 * Check if it is equal to the field element w in GF(p^2).
	 */
	public boolean isEqual(Fp2Field w)
	{
		return ((re.compareTo(w.re) == 0) && 
				(im.compareTo(w.im) == 0));
	}
	 
	/**
	 * Check if it is equal to the scalar a in GF(p).
	 */
	public boolean isEqual(BigInteger a)
	{
		return ((re.compareTo(a) == 0) &&
				(im.signum() == 0));
	}

	
	/**
	 * Get the characteristic of the base field GF(p).
	 */
	public BigInteger getP()
	{
		return p;
	}
	
	/**
	 * Get the size of the base field GF(p).
	 */
	public int getFieldSize()
	{
		return p.bitLength();
	}
	
}
