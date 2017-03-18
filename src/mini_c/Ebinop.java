package mini_c;

import java.util.LinkedList;

class Ebinop extends Expr {
  final Binop binop;
  final Expr expr1;
  final Expr expr2;

  Ebinop(Binop binop, Expr expr1, Expr expr2){
    super();
    this.binop = binop;
    this.expr1 = expr1;
    this.expr2 = expr2;
  }

  void semantic_analysis(LinkedList<String> errors){
    int number_of_errors_before = errors.size();

    expr1.semantic_analysis(errors);

    if(number_of_errors_before!=errors.size())
      return;

    expr2.semantic_analysis(errors);

    if(number_of_errors_before!=errors.size())
      return;

    if(binop == Binop.Baff){
      if(!(expr1 instanceof Elv))
        errors.add("Not a valid left value");
      else if(!expr2.type.equals("null") && !expr2.type.equals(expr1.type) && !(expr2.type.equals("void") && map_structs.containsKey(expr1.type)))
        errors.add("Incompatible types");
      else
        this.type = expr1.type;
    }
    else if(binop == Binop.Band || binop == Binop.Bor)
      this.type = "int";
    else if(binop == Binop.Badd  || binop == Binop.Bsub  || binop == Binop.Bmul  || binop == Binop.Bdiv){
      if(!expr1.type.equals("null") && !expr1.type.equals("int") && !expr2.type.equals("null") && !expr2.type.equals("int"))
        errors.add("Incompatible types: Expected integers");
      else
        this.type = "int";
    }
    else{
      if(!expr1.type.equals("null") && !expr2.type.equals("null") && !expr2.type.equals(expr1.type))
        errors.add("Incompatible types");
      else
        this.type = "int";
    }
  }
}
