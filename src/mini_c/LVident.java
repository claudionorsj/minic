package mini_c;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.Iterator;

class LVident extends Elv {
  final String ident;

  LVident (String ident) {
    this.ident = ident;
  }

  void semantic_analysis(LinkedList<String> errors){
    Iterator rit = list_context.descendingIterator();
    while(rit.hasNext()){
      HashMap<String,String> context = (HashMap<String,String>) rit.next();
      if(context.containsKey(ident)){
        type = context.get(ident);
        break;
      }
    }
    
    if(type.equals(""))
      errors.add("No variable " + ident);
  }
}
