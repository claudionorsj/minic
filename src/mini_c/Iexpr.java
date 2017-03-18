package mini_c;

import java.util.LinkedList;

class Iexpr extends Inst {
  final Expr expr;

  Iexpr(Expr expr){
    super();
    this.expr = expr;
  }

  void semantic_analysis(LinkedList<String> errors){
    expr.semantic_analysis(errors);
  }
}
