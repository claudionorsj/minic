package mini_c;

import java.util.LinkedList;

class Iwhile extends Inst {
  final Expr expr;
  final Inst inst;

  Iwhile(Expr expr, Inst inst){
    super();
    this.expr = expr;
    this.inst = inst;
  }

  void semantic_analysis(LinkedList<String> errors){}
}
