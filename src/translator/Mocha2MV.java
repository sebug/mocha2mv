package translator;

public class Mocha2MV {
	
	public static boolean DEBUG = false; // With global effect: do we want to see the processing output?

	/**
	 * prints out usage information.
	 */
	private static void usage() {
		System.out.println("usage: java translator.Mocha2MV inputfile.rm outputfile.mv");
	}
	/**
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		if(args.length != 2) {
			usage();
			System.exit(-1);
		}
		Translator t = new Translator(args[0],args[1]);
		try {
			t.translate();
		} catch (MochaParseException e) {
			System.err.println("Error parsing file " + args[0]);
		}
	}

}
