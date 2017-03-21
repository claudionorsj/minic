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
    for(Expr expr : list_expr)
      expr.semantic_analysis(errors);
    if(!map_fcts_params.containsKey(ident))
      errors.add("No function called " + ident);
    else if(map_fcts_params.get(ident).size()!=list_expr.size())
      errors.add("wrong number of arguments given " + list_expr.size() + " expected " + map_fcts_params.get(ident).size());
    else
      this.type = list_context.get(0).get(ident);
  }

  Label generate_rtl(Register value_r, Label next_l){
    LinkedList<Register> list_register = new LinkedList<Register>();
    for(Expr expr : list_expr)
      list_register.add(new Register());
    Label aux_l = current_rtlgraph.add(new Rcall(value_r,ident,list_register,next_l));
    for(int i = list_expr.size() - 1; i >= 0; i--)
      aux_l = list_expr.get(i).generate_rtl(list_register.get(i),aux_l);
    return aux_l;
  }
}
