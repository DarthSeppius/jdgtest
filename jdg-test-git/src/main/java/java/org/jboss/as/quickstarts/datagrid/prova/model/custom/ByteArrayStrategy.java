package org.jboss.as.quickstarts.datagrid.prova.model.custom;

import gnu.trove.strategy.HashingStrategy;

public class ByteArrayStrategy implements HashingStrategy<byte[]>{

	@Override
	public int computeHashCode(byte[] o) {
		byte[] c = (byte[])o; 
        // use the shift-add-xor class of string hashing functions 
        // cf. Ramakrishna and Zobel, "Performance in Practice 
        // of String Hashing Functions" 
        int h = 31; // seed chosen at random 
        for (int i = 0; i < c.length; i++) { // could skip invariants 
            h = h ^ ((h << 5) + (h >> 2) + c[i]); // L=5, R=2 works well for ASCII input 
        } 
        return h; 
	}

	@Override
	public boolean equals(byte[] o1, byte[] o2) {
		byte[] c1 = (byte[])o1; 
		byte[] c2 = (byte[])o2; 
        if (c1.length != c2.length) { // could drop this check for fixed-length keys 
            return false; 
        } 
        for (int i = 0, len = c1.length; i < len; i++) { // could skip invariants 
            if (c1[i] != c2[i]) { 
                return false; 
            } 
        } 
        return true; 
	}

}
