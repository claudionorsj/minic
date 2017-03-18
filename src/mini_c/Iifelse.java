package mini_c;

import java.util.LinkedList;

class Iifelse extends Inst {
  final Expr expr;
  final Inst inst_true;
  final Inst inst_false;

  Iifelse(Expr expr, Inst inst_true, Inst inst_false){
    super();
    this.expr = expr;
    this.inst_true = inst_true;
    this.inst_false = inst_false;
  }

  void semantic_analysis(LinkedList<String> errors){
    inst_true.semantic_analysis(errors);
    inst_false.semantic_analysis(errors);
    expr.semantic_analysis(errors);
  }
}
