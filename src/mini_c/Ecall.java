package mini_c;

import java.util.LinkedList;

class Ecall extends Expr {
  final String ident;
  final LinkedList<Expr> list_expr;

  Ecall(String ident, LinkedList<Expr> list_expr){
    super();
    this.ident = ident;
    this.list_expr = list_expr;
  }

  void semantic_analysis(LinkedList<String> errors){
    if(!map_fcts.containsKey(ident))
      errors.add("No function called " + ident);
    else if(map_fcts.get(ident).size()!=list_expr.size())
      errors.add("wrong number of arguments given " + list_expr.size() + " expected " + map_fcts.get(ident).size());
    else
      this.type = list_context.get(0).get(ident);
  }
}
