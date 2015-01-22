package org.bouncycastle.pqc.crypto.isogeny;

public class Fp2Queue {
	
	public Fp2Point P;
	public Fp2Queue next = null;
	public Fp2Queue prev = null;
	public int  h;
	
	public Fp2Queue()
	{
		next = null;
		prev = null;
		h    = 0;
		P    = null;
	}
	
	public Fp2Queue push(Fp2Queue tail)
	{
		tail.next = this;
		prev      = tail;
		return this;
	}
	
	public Fp2Queue pop()
	{
		if (prev!=null) 
			prev.next = null;
		
		return prev;
	}
	
}
