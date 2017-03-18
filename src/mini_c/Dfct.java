package mini_c;

import java.util.LinkedList;
import java.util.HashMap;

class Dfct extends Decl {
  final String type;
  final String ident;
  final LinkedList<Param> list_param;
  final Iblock block;

  Dfct(String type, String ident, LinkedList<Param> list_param, Iblock block){
    super();
    this.type = type;
    this.ident = ident;
    this.list_param = list_param;
    this.block = block;
  }

  void semantic_analysis(LinkedList<String> errors){
    HashMap<String,String> current_context = list_context.getLast();

    if(current_context.containsKey(ident))
      errors.add("Multiple declarations of" + ident);
    else if(!set_types.contains(type))
      errors.add("No type " + type);
    else{
      map_fcts.put(ident,list_param);
      current_context.put(ident,type);

      block.semantic_analysis(errors);
    }
  }
}
