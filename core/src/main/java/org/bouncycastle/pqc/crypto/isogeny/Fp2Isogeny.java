package org.bouncycastle.pqc.crypto.isogeny;


/**
 * Implementation of 2-, 3- and 4-isogenies closely following the C code
 * by Luca De Feo. (https://github.com/defeo/ss-isogeny-software/)
 */
public abstract class Fp2Isogeny 
{
  public abstract Fp2Point apply(Fp2Point P);

  /**
   * Isomorphisms of Montgomery curves.
   */
  public static class isom extends Fp2Isogeny 
  {
	  public Fp2Field u;
	  public Fp2Field r;
	  public Fp2Curve isocurve;
	
	  /**
	   * Compute an isomorphism of the Montgomery
	   * curve sending P to the point at infinity.
	   */
	  public isom(Fp2Curve curve, Fp2Point P)
	  {
		  Fp2Point P2;
		  Fp2Field x, z, P2x, a2, iA, iB;
		  
		  x	  = P.getX();
		  z   = P.getZ();
		  P2  = P.twice();
		  P2x = P2.getX().divide(P2.getZ());
		  r   = P2x.negate();
		  
		  a2 = P2x.multiply(Fp2Field.SCALAR_THREE).add(curve.getA());
		  u  = z.divide(x.subtract(z.multiply(P2x)));
		  iA = a2.multiply(u);
		  iB = curve.getB().multiply(u);
		  
		  isocurve = new Fp2Curve(iA, iB);	  
	  }
	  
	  /**
	   * Apply an isomorphism of Montgomery curves.
	   */
	  public Fp2Point apply(Fp2Point P)
	  {
		  Fp2Field Xout,Yout,Zout;
		  
		  Zout = P.getZ();
		  
		  if (P.getY()!=null)
			  Yout = P.getY().multiply(u);
		  else 
			  Yout = null;
		  
		  Xout = P.getX().add(r.multiply(P.getZ())).multiply(u);

		  return new Fp2Point(isocurve, Xout, Yout, Zout);
		  
	  }
	  
  }  
  
  /**
   * 2-isogenies of Montgomery curves.
   */
  public static class iso2 extends Fp2Isogeny 
  {
	  Fp2Field isoA;
	  Fp2Curve isocurve;
	  
	  /**
	   * Compute a 2-isogeny of the Montgomery curve
	   * sending P to (1,...).
	   */
	  public iso2(Fp2Curve curve,Fp2Point P)
	  {
		  Fp2Field x, z, xmz2, iB, iA;
		  
		  x    = P.getX();
		  z    = P.getZ();
		  xmz2 = x.subtract(z).square();
		  isoA = x.multiply(z).divide(xmz2);
		  
 		  iB   = curve.getB().multiply(isoA);
		  iA   = curve.getA().add(Fp2Field.SCALAR_SIX).multiply(isoA);
		  isocurve = new Fp2Curve(iA, iB);
	  }
	  
	  /**
	   * Apply a 2-isogeny of Montgomery curves.
	   */
	  public Fp2Point apply(Fp2Point P)
	  {
		  Fp2Field x, y, z, retX, retY = null, retZ, xmz2;
		  
		  x	   = P.getX();
		  y    = P.getY();
		  z    = P.getZ();
		  xmz2 = x.subtract(z).square();
		  retX = xmz2;
		  
		  if (y!=null)
		  {
			  Fp2Field X2;
			  
			  retX = x.multiply(xmz2);
			  X2   = x.square();
			  retY = isoA.multiply(y.multiply(X2.subtract(z.square())));
			  retZ = z.multiply(X2);
		  }
		  else
			  retZ = z.multiply(x);
		  
		  retX = retX.multiply(isoA);
	 
		  return new Fp2Point(isocurve, retX, retY, retZ);
	  }
	  
  }
  
  /**
   * 3-isogenies of Montgomery curves.
   */
  public static class iso3 extends Fp2Isogeny 
  {
	  Fp2Field isop;
	  Fp2Field isop2;
	  Fp2Curve isocurve;
	  
	 
	  /**
	   * Compute a 3-isogeny of the Montgomery curve.
	   */
	  public iso3(Fp2Curve curve, Fp2Point P)
	  {
		  Fp2Field AmSIXp, iA, iB;
		  
		  isop  = P.getX().divide(P.getZ());
		  isop2 = isop.square();
		  
 		  AmSIXp = curve.getA().subtract(isop.multiply(Fp2Field.SCALAR_SIX));
		  iA     = AmSIXp.multiply(isop).add(Fp2Field.SCALAR_SIX).multiply(isop);
		  iB     = curve.getB().multiply(isop2);
		  
		  isocurve = new Fp2Curve(iA, iB);
	  }
	  
	  /**
	   * Apply a 3-isogeny of Montgomery curves.
	   */
	  public Fp2Point apply(Fp2Point P)
	  {
		  Fp2Field x, y, z, Xout, Yout = null, Zout, h, rh;
		  
		  x    = P.getX();
		  y    = P.getY();
		  z    = P.getZ();
		  h    = x.subtract(isop.multiply(z));
		  rh   = x.multiply(isop).subtract(z);
		  Xout = x.multiply(rh.square());
 		  
		  if (y!=null)
		  {
			  Fp2Field temp;
			  Xout = Xout.multiply(h);
			  temp = x.multiply(z).multiply(Fp2Field.ONE.subtract(isop2));
			  temp = temp.add(temp);
			  temp = rh.multiply(rh.multiply(h).add(temp)); 
			  Yout = y.multiply(temp);
			  Zout = h.square().multiply(h).multiply(z);
		  }
		  else
			  Zout = h.square().multiply(z);
		  
		  return new Fp2Point(isocurve, Xout, Yout, Zout);
	  }
	  
  }
  
  /**
   * 4-isogenies of Montgomery curves.
   */
  public static class iso4 extends Fp2Isogeny 
  {  
	  Fp2Field isoAp2;
	  Fp2Curve isocurve;

	  /**
	   * Compute a 4-isogeny of the Montgomery curve
	   * sending (1,...) to infinity.
	   */
	  public iso4(Fp2Curve curve)
	  {
		  Fp2Field a, iAm2, iA, iB;
		  
		  a		 = curve.getA();
		  isoAp2 = a.add(Fp2Field.SCALAR_TWO);	
		  iAm2   = (Fp2Field.TWO.subtract(a)).invert();
		  iA     = ((a.add(Fp2Field.SCALAR_SIX)).multiply(iAm2)).multiply(Fp2Field.SCALAR_TWO).negate();
		  iB     = curve.getB().multiply(iAm2);
		  
		  isocurve = new Fp2Curve(iA,iB);
	  }
	  
	  /**
	   * Apply a 4-isogeny of Montgomery curves.
	   */
	  public Fp2Point apply(Fp2Point P)
	  {
		  Fp2Field x, y, z, Xout, Yout = null, Zout, z1, x1, zA2, FOURz;
		  
		  x     = P.getX();
		  y     = P.getY();
		  z     = P.getZ();
		  z1    = x.multiply(z);
		  x1    = (x.subtract(z)).square();
		  zA2   = z1.multiply(isoAp2);
		  FOURz = z1.multiply(Fp2Field.SCALAR_FOUR);
		  Xout  =(x1.add(zA2)).multiply(x1.add(FOURz));
		  
		  if (y!=null)
		  {
			  Fp2Field B, C, D;
			  
			  B    = x.multiply(x1);
			  Xout = Xout.multiply(B);
			  C    = x1.add(z1.subtract(z.square()).multiply(Fp2Field.SCALAR_TWO));
			  D    = x1.square().subtract(zA2.multiply(FOURz));
			  Yout = y.multiply(C).multiply(D);
			  Zout = z.multiply(B.square()).multiply(Fp2Field.FOUR.subtract(isoAp2));
		  }
		  else
			  Zout = x1.multiply(FOURz.subtract(isoAp2));
		  
		  return new Fp2Point(isocurve, Xout, Yout, Zout);
		  
	  }
	  
  }
  
}
