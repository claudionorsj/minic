package mini_c;

import java.util.LinkedList;

class Emin extends Expr {
  final Expr expr;

  Emin(Expr expr){
    super();
    this.expr = expr;
  }

  void semantic_analysis(LinkedList<String> errors){
    int number_of_errors_before = errors.size();

    expr.semantic_analysis(errors);

    if(number_of_errors_before!=errors.size())
      return;

    if(!expr.type.equals("null") && !expr.type.equals("int"))
      errors.add("cannot use operator minus on expression of type " + expr.type);
    else
      this.type = "int";
  }
}
