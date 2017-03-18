package mini_c;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.Iterator;

class Esizeof extends Expr {
  final String ident;

  Esizeof(String ident){
    super();
    this.ident = ident;
  }

  void semantic_analysis(LinkedList<String> errors){
    Iterator rit = list_context.descendingIterator();
    String ident_type = "";

    while(rit.hasNext()){
      HashMap<String,String> context = (HashMap<String,String>) rit.next();
      if(context.containsKey(ident)){
        ident_type = context.get(ident);
        break;
      }
    }
    
    if(ident_type.equals(""))
      errors.add("No variable " + ident);
    else if(!map_structs.containsKey(ident))
      errors.add(ident + " is not a struct");
    else
      type = "int";
  }
}
