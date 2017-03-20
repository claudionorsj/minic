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
    Iterator<HashMap<String,String>> rit = list_context.descendingIterator();
    while(rit.hasNext()){
      HashMap<String,String> context = rit.next();
      if(context.containsKey(ident)){
        this.type = context.get(ident);
        break;
      }
    }
    
    if(type.equals(""))
      errors.add("No variable " + ident);
    else if(!type.equals("int") && !type.equals("void") && !type.equals("null") && !map_structs_lists_field.containsKey(type))
      errors.add("No struct " + type);

  }

  Label generate_rtl(Register value_r, Label next_l){
    String aux_s;
    if(map_locals_registers.containsKey(ident))
      return current_rtlgraph.add(new Rmbinop(Mbinop.Mmov,map_locals_registers.get(ident),value_r,next_l));
    else
      return current_rtlgraph.add(new Raccess_global(ident,value_r,next_l));
  }
}
