package mini_c;

import java.util.LinkedList;
import java_cup.runtime.*;

public class Main{
	public static void main(String[] args) throws Exception{
		boolean parse_only = false;
		boolean type_only = false;
		String file = "../test.c";
		for(String s : args){
			if (s.equals("--parse-only"))
				parse_only = true;
			else if (s.equals("--type-only"))
				type_only = true;
			else
				file = s;
		}
		java.io.Reader reader = new java.io.FileReader(file);
		Lexer lexer = new Lexer(reader);
		Parser parser = new Parser(lexer);
		// performs parsing and syntax analysis
		File f = (File) parser.parse().value;

		// ends execution is parse_only is true
		if(parse_only)
			return;

		LinkedList<String> errors = new LinkedList<String>();
		// performs the semantic analysis
		f.semantic_analysis(errors);
		// if there is any error, print and then return 1
		for(String error : errors)
			System.err.println(error);
		if(errors.size() != 0)
			System.exit(1);

		// ends execution is type_only is true
		if(type_only)
			return;

		// preforms the compilation
		RTLfile rtlfile = f.generate_rtl();
		ERTLfile ertlfile = (new ToERTL()).translate_file(rtlfile);
		LTLfile ltlfile = (new ToLTL()).translate_file(ertlfile);
		X86_64 asm = (new Lin()).translate_file(ltlfile);
		asm.printToFile(file.substring(0, file.lastIndexOf('.')).concat(".s"));
	}
}
