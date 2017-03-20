package mini_c;

import java.util.LinkedList;
import java.util.HashMap;

class Param extends Base {
  String type;
  String ident;

  Param(String type, String ident){
    this.type = type;
    this.ident = ident;
  }

  void semantic_analysis(LinkedList<String> errors){
    HashMap<String,String> current_context = list_context.getLast();

    if(current_context.containsKey(ident))
      errors.add("Multiple declarations of" + ident);
    else if(!set_types.contains(type))
      errors.add("No type " + type);
    else
      current_context.put(ident,type);
  }

  Label generate_rtl(){return null;}
}
