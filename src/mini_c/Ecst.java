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
}
