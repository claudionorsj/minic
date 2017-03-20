package mini_c;

import java.util.LinkedList;

class Ecst extends Expr {
  final Integer cst;

  Ecst(Integer cst){
    super();
    this.cst = cst;
  }

  void semantic_analysis(LinkedList<String> errors){
    if(cst.equals(0))
      this.type = "null";
    else
      this.type = "int";
  }

  Label generate_rtl(Register value_r, Label next_l){
    return current_rtlgraph.add(new Rconst(cst,value_r,next_l));
  }
}
