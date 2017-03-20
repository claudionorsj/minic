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
    if(!map_structs_lists_field.containsKey(ident))
      errors.add("No struct " + ident);
    else
      type = "int";
  }

  Label generate_rtl(Register value_r, Label next_l){
    return current_rtlgraph.add(new Rconst(8*map_structs_lists_field.get(ident).size(),value_r,next_l));
  }
}
