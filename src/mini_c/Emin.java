package mini_c;

import java.util.LinkedList;

class Emin extends Expr {
  final Expr expr;

  Emin(Expr expr){
    super();
    this.expr = expr;
  }

  void semantic_analysis(LinkedList<String> errors){
    expr.semantic_analysis(errors);

    if(!expr.type.equals("null") && !expr.type.equals("int"))
      errors.add("cannot use operator minus on expression of type " + expr.type);
    else
      this.type = "int";
  }

  Label generate_rtl(Register value_r, Label next_l){
    Register aux_r = new Register();
    Label aux_l = current_rtlgraph.add(new Rmbinop(Mbinop.Msub,aux_r,value_r,next_l));
    aux_l = current_rtlgraph.add(new Rconst(0,value_r,aux_l));
    return expr.generate_rtl(aux_r,aux_l);
  }
}
