package org.jboss.as.quickstarts.datagrid.prova.model.custom;

import gnu.trove.strategy.HashingStrategy;

public class CharArrayStrategy implements HashingStrategy<char[]>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4284936983145398525L;

	@Override
	public int computeHashCode(char[] o) {
		char[] c = (char[])o; 
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
	public boolean equals(char[] o1, char[] o2) {
		char[] c1 = (char[])o1; 
        char[] c2 = (char[])o2; 
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
