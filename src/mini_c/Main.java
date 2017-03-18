package mini_c;

import java.util.LinkedList;

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
		// syntax analysis
		File f = (File) parser.parse().value;

		if(!parse_only){
			LinkedList<String> errors = new LinkedList<String>();
			//semantic_analysis
			f.semantic_analysis(errors);
			for(String error : errors)
				System.err.println(error);
			if(errors.size() != 0)
				System.exit(1);
		}
	}
}
