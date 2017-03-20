package mini_c;

import java.util.LinkedList;

class Enot extends Expr {
  final Expr expr;

  Enot(Expr expr){
    super();
    this.expr = expr;
  }

  void semantic_analysis(LinkedList<String> errors){
    expr.semantic_analysis(errors);

    this.type = "int";
  }

  Label generate_rtl(Register value_r, Label next_l){
    Register aux_r = new Register();
    Label aux_l = current_rtlgraph.add(new Rmubranch(new Mjz(),aux_r,current_rtlgraph.add(new Rconst(1,value_r,next_l)),current_rtlgraph.add(new Rconst(0,value_r,next_l))));
    return expr.generate_rtl(aux_r,aux_l);
  }
}
