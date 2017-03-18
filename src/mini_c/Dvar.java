package mini_c;

import java.util.LinkedList;
import java.util.HashMap;

class Dvar extends Decl {
  final String type;
  final LinkedList<String> list_ident;

  Dvar(String type, LinkedList<String> list_ident){
    super();
    this.type = type;
    this.list_ident = list_ident;
  }

  void semantic_analysis(LinkedList<String> errors){
    //gets the current context for type check
    HashMap<String,String> current_context = list_context.getLast();

    for(String ident : list_ident){
      // adds error if identifier already used in the current context
      if(current_context.containsKey(ident))
        errors.add("Multiple declarations of " + ident);
      // adds error if the type is not recognized
      else if(!set_types.contains(type))
        errors.add("No type " + type);
      // adds the variable with its type to the current context if no error was found
      else
        current_context.put(ident,type);
    }
  }
}
