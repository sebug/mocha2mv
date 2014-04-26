package translator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Date;

import mocha.parser.ASTDefList;
import mocha.parser.ParseException;
import mocha.parser.PrsParser;
import blifmv.ModelCollection;

public class Translator {
	/** Name of the file to translate **/
	private final String filename;
	/** The content of the file **/
	private final FileInputStream content;
	
	private final PrintStream out;
	
	public Translator(String filename, String outfilename) {
		this.filename = filename;
		try {
			this.content = new FileInputStream(filename);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Error opening file " + filename + ".");
		}
		try {
			this.out = new PrintStream(new File(outfilename));
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Error opening output file " + outfilename+ ".");
		}
	}
	
	/**
	 * Translates the file and puts the output to stdout.
	 * @throws MochaParseException 
	 */
	public void translate() throws MochaParseException {
		try {
			ASTDefList adl = PrsParser.parse(content);
			// Now we got the abstract syntax tree. Let's traverse and output it.
			if( Mocha2MV.DEBUG ) {
				adl.dump("#");
				System.out.println("=== Now creating the data structures ===");
			}
			ModelCollection mc = (ModelCollection)adl.jjtAccept(new TypeCollector(), null);
			adl.jjtAccept(new TranslatorVisitor(), mc);
			
			// Now print the MV file
			out.println("# BLIF-MV generated from input file " + this.filename);
			out.println("# on " + (new Date(System.currentTimeMillis())));
			out.println("# With mocha2mv version 0.1");
			out.println();
			
			mc.printMV(out);
		} catch (ParseException e) {
			throw new MochaParseException();
		}
	}
}
