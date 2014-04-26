package blifmv;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import translator.Mocha2MV;
import expr.And;
import expr.BooleanConstant;
import expr.ArithExpression;
import expr.Equality;
import expr.Variable;

/**
 * Represents a table from some inputs to some outputs.
 * The order will be important later on when interpreting the guard formulas.
 */
public class Table {
	private final Model associatedModel;
	
	private final List<Var> inputVars = new LinkedList<Var>();
	private final List<Var> outputVars = new LinkedList<Var>();
	private String tableName;
	
	private List<List<String>> resetTable;
	private List<List<String>> updateTable;
	
	private int nondeterminismCount = 1; // How many different states we need for auxiliary nondeterminism variable.
	private static int nondetVarNumber = 0; // The current number of auxiliary var.
	private Var myNondetVar = null;
	private Var myResetNondetVar = null; // Variable used if the reset table is nondeterministic but depending
	private int resetNondeterminismCount = 1;	// on input variables.
	
	public final boolean isLazy;
	public final boolean isEvent;
	
	public Table(boolean isLazy, boolean isEvent, Model associatedModel) {
		this.isLazy = isLazy;
		this.isEvent = isEvent;
		this.associatedModel = associatedModel;
	}
	
	/**
	 * Adds an input variable that will be used by this table.
	 * @param v the variable to add.
	 */
	public void addInput(Var v) {
		if( !this.inputVars.contains(v) ) // Don't add it twice.
			this.inputVars.add(v);
	}
	
	/**
	 * Adds an output that will be written by this table.
	 * @param v the variable to add.
	 */
	public void addOutput(Var v) {
		if( !this.outputVars.contains(v))
			this.outputVars.add(v);
	}
	
	public List<Var> getInputVars() {
		return this.inputVars;
	}
	
	/**
	 * Returns the input variables that are not controlled,
	 * leaving out the other ones. This is needed to define
	 * the reset table (because we can't use any information
	 * from the controlled variables there).
	 * @return input variables not appearing in the output.
	 */
	public List<Var> getSafeInputVars() {
		List<Var> ret = new LinkedList<Var>();
		for(Var v : this.inputVars ) {
			boolean isSafe = true;
			for(Var x: this.outputVars ) {
				if(v.prime().getName().equals(x.getName())) isSafe = false;
			}
			if( isSafe ) ret.add(v);
		}
		return ret;
	}
	
	public List<Var> getOutputVars() {
		return this.outputVars;
	}
	
	/**
	 * Prints the blif-mv representation of this table.
	 * @param out where to write.
	 */
	public void printMV(PrintStream out) {
		out.println("# " + this.tableName);
		
		printResetTables(out);
		
		printUpdateTable(out);
	}

	/**
	 * Prints the update table.
	 * @param out Where to print.
	 */
	private void printUpdateTable(PrintStream out) {
		// Finally, print the update tables.
		out.print(".table ");
		if( this.nondeterminismCount > 1) {
			// First input variable is the nondeterministic one now.
			out.print(this.myNondetVar.getName()+" ");
		}
		for(Var input : this.inputVars) {
			out.print(input.getName() + " ");
		}
		out.print(" -> ");
		for(Var output: this.outputVars) {
			out.print(output.getName()+ " ");
		}
		out.println();
		
		List<List<String>> seen = new LinkedList<List<String>>();
		int n = this.inputVars.size();
		for(List<String> line: this.updateTable) {
			if( this.nondeterminismCount > 1) {
				// Now we have to number each line according to the occurence of such strings until now
				int associatedNdNumber = 0;
				for(List<String> s : seen) {
					if( firstNPositionsMatch(line, s, n)) associatedNdNumber++;
				}
				out.print(associatedNdNumber + " ");
			}
			for(String el: line) {
				out.print(el + " ");
			}
			out.println();
			seen.add(line);
		}
		
		out.println("# Filling in the unused nondet combinations.");
		if( this.nondeterminismCount > 1) {
			// Keep track of up to which index we used the nondeterminism variables.
			// For the rest, we will have to print additional lines.
			Map<String,Integer> occurenceUpTo = new HashMap<String, Integer>();
			for(List<String> line: this.updateTable) {
				String res = joinN(line,n);
				
				incrementKey(occurenceUpTo,res);
			}
			
			for(List<String> line : this.updateTable) {
				// First, generate the key.
				String key = joinN(line,n);
				// Now output this last combination if not yet maximal
				int index = occurenceUpTo.get(key);
				if( index < this.nondeterminismCount - 1 ) {
					out.print(range(index + 1, this.nondeterminismCount - 1) + " ");
					for(String s: line) {
						out.print(s + " ");
					}
					out.println();
					occurenceUpTo.put(key, this.nondeterminismCount - 1);
				}
			}
		}
	}

	/**
	 * Prints the reset tables (one for each latch controlled).
	 * @param out where to print.
	 */
	private void printResetTables(PrintStream out) {
		// Now, print the reset table. Note that the reset table is only valid for one output, so we'll have
		// To create multiple reset statements if the table contains multiple outputs.
		int pos = 0;
		for(Var output: this.outputVars) {
			// Print the reset line
			out.print(".reset ");
			if( this.resetNondeterminismCount > 1) {
				// Nondeterministic pseudo-inputs needed 
				out.print(this.myResetNondetVar.getName() + " ");
			}
			for(Var safe: this.getSafeInputVars()) {
				out.print(safe.getName() + " ");
			}
			out.println(output.unprime().getName());
			
			// Print the table.
			List<List<String>> seen = new LinkedList<List<String>>();
			int n = this.getSafeInputVars().size();
			for(List<String> line: this.resetTable) {
				List<String> completeLine = new LinkedList<String>();
				for(int i = 0; i < n; i++) {
					completeLine.add(line.get(i));
				}
				completeLine.add(line.get(n + pos)); // add the controlled part
				
				if( !isIn(seen,completeLine) ) {
					// First, determine the nondeterminism count of this line
					if( this.resetNondeterminismCount > 1) {
						int nondeterminismCount = 0;
						for( List<String> seenLine: seen ) {
							if( firstNPositionsMatch(seenLine, completeLine, n)) {
								nondeterminismCount++;
							}
						}
						out.print(nondeterminismCount + " ");
					}
					for(String el: completeLine) {
						out.print(el + " ");
					}
					out.println();
					seen.add(completeLine);
				}
			}
			
			// Add the unused nondeterminism combinations
			// TODO: There is much similarity between reset and update tables that could be extracted.
			if( this.resetNondeterminismCount > 1 ) {
				out.println("# Completing reset table with unused combinations.");
				Map<String,Integer> occurenceUpTo = new HashMap<String, Integer>();
				// Now iterate over all lines, counting how many times this line already occured.
				// Finally, add the missing combinations.
				for( List<String> line: seen) {
					String res = joinN(line,n);
					incrementKey(occurenceUpTo,res);
				}
				
				// Now print the missing combinations
				for( List<String> line : seen ) {
					String key = joinN(line,n);
					
					int index = occurenceUpTo.get(key);
					if( index < this.resetNondeterminismCount - 1) {
						out.print(range(index + 1, this.resetNondeterminismCount - 1) + " ");
						out.print(key + " ");
						out.println(line.get(n) + " "); // The controlled var.
					}
					occurenceUpTo.put(key, this.resetNondeterminismCount - 1); // We treated this case.
				}
			}
			pos++;
		}
	}
	
	/**
	 * The range notation as used in BLIF-MV
	 * @param from from where to begin (included)
	 * @param to up to which value (included)
	 * @return the string representation of this range.
	 */
	private String range(int from, int to) {
		return "{" + from + "-" + to + "}";
	}
	
	/**
	 * Joins the first n elements of a list to a string, space at the end.
	 * @param line the list of input strings
	 * @param n how many to take
	 * @return a string from the first n elements, with a space at the end.
	 */
	private String joinN(List<String> line, int n) {
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for(String col: line) {
			if( i < n ) sb.append(col + " ");
			i++;
		}
		return sb.toString();
	}
	
	/**
	 * Increments the value associated with a key or adds one if none is there.
	 * @param map On which map to apply this
	 * @param key Which key to increment.
	 */
	private void incrementKey(Map<String,Integer> map, String key) {
		if( map.containsKey(key) ) {
			map.put(key, map.get(key) + 1);
		} else {
			map.put(key, 0);
		}
	}
	
	/**
	 * Checks whether the list of strings is in this list of list of strings.
	 * @param els The list of list of strings
	 * @param el the list of strings
	 */
	private boolean isIn(List<List<String>> els, List<String> el) {
		boolean isThere = false;
		for( List<String> el_to_compare : els) {
			boolean theSame = true;
			for(int i = 0; i < el_to_compare.size() && theSame; i++) {
				if( !el_to_compare.get(i).equals(el.get(i)) ) {
					theSame = false;
				}
			}
			if( theSame ) {
				isThere = true;
			}
		}
		return isThere;
	}
	
	/**
	 * Changes the table name (only used in comments on code generation)
	 * @param tableName former name of the atom.
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setResetAction(List<List<String>> inittable) {
		this.resetTable = inittable;
		// Determine reset table nondeterministic input size.
		int numberOfInputVars = this.getSafeInputVars().size();
		for( List<String> line: this.resetTable ) {
			int same = 0;
			for( List<String> compared: this.resetTable ) {
				if( this.firstNPositionsMatch(line, compared, numberOfInputVars)) {
					same++; // Same combination of input vars - different output.
				}
			}
			if( same > this.resetNondeterminismCount ) {
				this.resetNondeterminismCount = same;
			}
		}
		if( this.resetNondeterminismCount > 1) {
			// Nondeterminism needed.
			int varNumber = Table.nondetVarNumber++;
			this.myResetNondetVar = new Var("nd"+varNumber,new SequenceType("ND" + varNumber, this.resetNondeterminismCount));
			this.associatedModel.addNondetVar(this.myResetNondetVar);
		}
	}

	public void setUpdateAction(List<List<String>> updatetable) {
		this.updateTable = updatetable;
		
		// if it is lazy, then we will have to add the stay directives
		if( this.isLazy ) {
			// Atom may sleep in every update round
			// this translates to another line in the table
			// where every primed variable retains the value from the unprimed one.
			if( Mocha2MV.DEBUG ) {
				System.out.println("Table " + this.tableName + " is lazy.");
			}
			ValueIterator allCombinations = new ValueIterator(this.inputVars);
			while(allCombinations.hasNext()) {
				List<String> valuation = allCombinations.next();
				for(Var v: this.outputVars) {
					valuation.add("="+v.unprime().getName()); // stays the same.
				}
				this.updateTable.add(valuation);
			}
		} else if ( this.isEvent ) {
			// A passive atom oh my.
			// Basically, in all cases where no observable event occurs, we may sleep.
			// We can express this with a formula
			System.err.println("Input atom " + this.tableName + " is passive. This might produce an ambiguous specification.");
			ArithExpression noChangeCondition = BooleanConstant.TRUE;
			for(Var v : this.inputVars) {
				if( v.isPrimed() ) {
					noChangeCondition = new And(noChangeCondition,
							new Equality(new Variable(v.unprime()),new Variable(v.prime()))); // v = v'
				}
			}
			ValueIterator allCombinations = new ValueIterator(this.inputVars);
			while(allCombinations.hasNext()) {
				List<String> valuation = allCombinations.next();
				if( noChangeCondition.evaluate(this.inputVars, valuation).equals("1") ) {
					for(Var v: this.outputVars) {
						valuation.add("=" + v.unprime().getName()); // stays the same
					}
					this.updateTable.add(valuation);
				}
			}
		}
		
		// Now we need to check whether the table contains duplicate entries with the same key.
		int n = this.inputVars.size();
		for(List<String> outer: this.updateTable) {
			int sames = 0; // At the moment there is no line the same
			for(List<String> inner: this.updateTable) {
				if(firstNPositionsMatch(outer, inner, n)) {
					sames++;
				}
			}
			
			if( sames > this.nondeterminismCount) {
				this.nondeterminismCount = sames;
			}
		}
		
		if( this.nondeterminismCount > 1) {
			// We need a pseudo-input-variable
			int myNondetVarNumber = Table.nondetVarNumber++;
			this.myNondetVar = new Var("nd"+myNondetVarNumber,new SequenceType("ND"+myNondetVarNumber,this.nondeterminismCount));
			this.associatedModel.addNondetVar(this.myNondetVar);
		}
	}
	
	/**
	 * Tells whether the beginning of two lines are the same
	 * @param line1 the first line
	 * @param line2 the second line
	 * @param n How many positions to care about
	 * @return whether the first n positions match
	 */
	private boolean firstNPositionsMatch(List<String> line1, List<String> line2, int n) {
		boolean theyMatch = true;
		for(int i = 0; i < n && theyMatch; i++) {
			String s1 = line1.get(i);
			String s2 = line2.get(i);
			if( !s1.equals(s2) ) theyMatch = false;
		}
		return theyMatch;
	}
}
