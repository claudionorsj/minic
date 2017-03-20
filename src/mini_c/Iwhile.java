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

  void semantic_analysis(LinkedList<String> errors){
    inst.semantic_analysis(errors);
    expr.semantic_analysis(errors);
  }

  Label generate_rtl(Label next_l, Register return_r, Label return_l){
    Label end_l = new Label();
    Label entry_l = expr.generate_rtl_c(inst.generate_rtl(end_l,return_r,return_l),next_l);
    current_rtlgraph.graph.put(end_l,new Rgoto(entry_l));
    return entry_l;
  }
}
