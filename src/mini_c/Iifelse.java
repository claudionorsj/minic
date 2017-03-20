package mini_c;

import java.util.LinkedList;

class Iifelse extends Inst {
  final Expr expr;
  final Inst inst_true;
  final Inst inst_false;

  Iifelse(Expr expr, Inst inst_true){
    super();
    this.expr = expr;
    this.inst_true = inst_true;
    this.inst_false = null;
  }

  Iifelse(Expr expr, Inst inst_true, Inst inst_false){
    super();
    this.expr = expr;
    System.out.println(expr);
    this.inst_true = inst_true;
    this.inst_false = inst_false;
  }

  void semantic_analysis(LinkedList<String> errors){
    expr.semantic_analysis(errors);
    inst_true.semantic_analysis(errors);
    if(inst_false != null)
      inst_false.semantic_analysis(errors);
  }

  Label generate_rtl(Label next_l, Register return_r, Label return_l){
    Label true_l = inst_true.generate_rtl(next_l,return_r,return_l);
    Label false_l;
    if(inst_false == null)
      false_l = next_l;
    else
      false_l = inst_false.generate_rtl(next_l,return_r,return_l);
    return expr.generate_rtl_c(true_l, false_l);
  }
}
