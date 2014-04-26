package blifmv;

/**
 * Represents a variable.
 *
 */
public class Var {
	private final String name;
	private final Type type;
	private final boolean isPrimed;
	
	public Var(String name, Type type) {
		this.name = name;
		this.type = type;
		this.isPrimed = false;
	}
	
	private Var(String name, Type type, boolean isPrimed) {
		this.name = name;
		this.type = type;
		this.isPrimed = isPrimed;
	}
	
	/**
	 * The primed version of a variable.
	 * @return
	 */
	public Var prime() {
		return new Var(this.name,this.type,true);
	}
	
	/**
	 * Returns whether the variable is primed.
	 */
	public boolean isPrimed() {
		return this.isPrimed;
	}
	
	/**
	 * Returns the name, adds a prime if the variable is primed.
	 * @return
	 */
	public String getName() {
		if( isPrimed ) {
			return this.name + "'";
		} else {
			return this.name;
		}
	}
	
	public Type getType() {
		return this.type;
	}

	@Override
	public boolean equals(Object obj) {
		if( !(obj instanceof Var) ) return false;
		else {
			Var v2 = (Var)obj;
			return this.name.equals(v2.name) && this.type.equals(v2.type) && (this.isPrimed == v2.isPrimed);
		}
	}
	
	@Override
	public int hashCode() {
		int factor = 43;
		int ret = this.name.hashCode();
		ret *= factor;
		ret += this.type.hashCode();
		ret *= factor;
		if( this.isPrimed ) {
			ret += 1;
		}
		return ret;
	}

	@Override
	public String toString() {
		return this.getName() + " : " + this.type;
	}

	/**
	 * Returns an object where the variable is not primed.
	 * @return
	 */
	public Var unprime() {
		return new Var(this.name,this.type,false);
	}
}
