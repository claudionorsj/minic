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
    int number_of_errors_before = errors.size();

    expr.semantic_analysis(errors);

    if(number_of_errors_before == errors.size()){
      HashMap<String,String> map_fields = map_structs.get(expr.type);

      if(!map_fields.containsKey(ident))
        errors.add("No field " + ident + " in struct" + expr.type);
      else
        this.type = map_fields.get(ident);
    }
  }
}
