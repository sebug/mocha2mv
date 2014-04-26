package blifmv;

import java.util.List;

/**
 * Represents a type from a Reactive Modules variable,
 * which can directly be taken over to BLIF-MV (with some
 * ingenuity, that is)
 *
 */
public abstract class Type {
	private final String name;
	
	public Type(String name) {
		this.name = name;
	}
	
	/**
	 * Returns how many elements there are in this type.
	 * @return
	 */
	abstract public int getNumElements();
	/**
	 * Returns the type representation to be used in multivariable declaration.
	 * @return
	 */
	abstract public String getTypeRepresentation();
	
	abstract public List<String> getAllValues();
	
	abstract public String get(int index);
	
	abstract public String getDefaultValue();
	
	abstract public int getValPos(String val);

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
	
	@Override
	public boolean equals(Object that) {
		if( that instanceof Type ) {
			Type o2 = (Type)that;
			return this.name.equals(o2.name);
		} else {
			return false;
		}
	}

	/**
	 * Returns the name of the type.
	 * @return the name of this type.
	 */
	public String getName() {
		return this.name;
	}
}
