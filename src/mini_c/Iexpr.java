package mini_c;

import java.util.LinkedList;

class Iexpr extends Inst {
  final Expr expr;

  Iexpr(){
    super();
    this.expr = null;
  }

  Iexpr(Expr expr){
    super();
    this.expr = expr;
  }

  void semantic_analysis(LinkedList<String> errors){
    if(expr != null)
      expr.semantic_analysis(errors);
  }

  Label generate_rtl(Label next_l, Register return_r, Label return_l){
    if(expr == null)
      return next_l;
    Register aux_r = new Register();
    Label aux_l = expr.generate_rtl(aux_r,next_l);
    return aux_l;
  }
}
