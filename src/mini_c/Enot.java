package mini_c;

import java.util.LinkedList;

class Enot extends Expr {
  final Expr expr;

  Enot(Expr expr){
    super();
    this.expr = expr;
  }

  void semantic_analysis(LinkedList<String> errors){
    int number_of_errors_before = errors.size();

    expr.semantic_analysis(errors);

    if(number_of_errors_before!=errors.size())
      return;

    this.type = "int";
  }
}
