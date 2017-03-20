package mini_c;

import java.util.LinkedList;

class Ireturn extends Inst {
  final Expr expr;

  Ireturn(Expr expr){
    super();
    this.expr = expr;
  }

  void semantic_analysis(LinkedList<String> errors){
    expr.semantic_analysis(errors);
  }

  Label generate_rtl(Label next_l, Register return_r, Label return_l){
    return expr.generate_rtl(return_r,return_l);
  }
}
