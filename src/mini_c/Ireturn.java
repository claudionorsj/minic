package mini_c;

import java.util.LinkedList;

class Ireturn extends Inst {
  final Expr expr;

  Ireturn(Expr expr){
    super();
    this.expr = expr;
  }

  void semantic_analysis(LinkedList<String> errors){}
}
