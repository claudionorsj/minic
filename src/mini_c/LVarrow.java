package mini_c;

import java.util.LinkedList;
import java.util.HashMap;

class LVarrow extends Elv {
  final Expr expr;
  final String ident;

  LVarrow (Expr expr, String ident) {
    this.expr = expr;
    this.ident = ident;
  }

  void semantic_analysis(LinkedList<String> errors){
    expr.semantic_analysis(errors);

    HashMap<String,String> map_fields_types = map_structs_maps_fields_types.get(expr.type);

    if(!map_fields_types.containsKey(ident))
      errors.add("No field " + ident + " in struct" + expr.type);
    else
      this.type = map_fields_types.get(ident);
  }

  Label generate_rtl(Register value_r, Label next_l){
    String type = expr.type;
    LinkedList<String> list_field = map_structs_lists_field.get(expr.type);
    Register aux_r = new Register();
    Label aux_l = current_rtlgraph.add(new Rload(aux_r,list_field.indexOf(ident),value_r,next_l));
    return expr.generate_rtl(aux_r,aux_l);
  }
}
