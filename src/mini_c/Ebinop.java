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
    expr1.semantic_analysis(errors);

    expr2.semantic_analysis(errors);

    if(binop == Binop.Baff){
      if(!(expr1 instanceof Elv))
        errors.add("Not a valid left value");
      else if(!expr2.type.equals("null") && !expr2.type.equals(expr1.type) && !(expr2.type.equals("void") && map_structs_maps_fields_types.containsKey(expr1.type)))
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

  Label generate_rtl(Register value_r, Label next_l){
    if(binop == Binop.Baff){
      Label aux_l;
      if(expr1 instanceof LVident){
        String var_ident = ((LVident)expr1).ident;
        String aux_s;
        if(map_locals_registers.containsKey(var_ident))
          aux_l = current_rtlgraph.add(new Rmbinop(Mbinop.Mmov,value_r,map_locals_registers.get(var_ident),next_l));
        else
          aux_l = current_rtlgraph.add(new Rassign_global(value_r,var_ident,next_l));
      }
      else{
        LVarrow lvarrow = ((LVarrow)expr1);
        Register aux_r = new Register();
        LinkedList<String> list_field = map_structs_lists_field.get(lvarrow.expr.type);
        aux_l = current_rtlgraph.add(new Rstore(value_r,aux_r,list_field.indexOf(lvarrow.ident),next_l));
        aux_l = lvarrow.expr.generate_rtl(aux_r, aux_l);
      }
      return expr2.generate_rtl(value_r,aux_l);
    }
    else if(binop == Binop.Band || binop == Binop.Bor || binop == Binop.Blt || binop == Binop.Ble || binop == Binop.Bgt || binop == Binop.Bge || binop == Binop.Beq || binop == Binop.Bne)
      return generate_rtl_c(current_rtlgraph.add(new Rconst(1,value_r,next_l)),current_rtlgraph.add(new Rconst(0,value_r,next_l)));
    else{
      if(expr1 instanceof Ecst && expr2 instanceof Ecst && binop != Binop.Bdiv){
        int v1 = ((Ecst)expr1).cst;
        int v2 = ((Ecst)expr2).cst;
        if(binop == Binop.Badd)
          return current_rtlgraph.add(new Rconst(v1+v2,value_r,next_l));
        else if(binop == Binop.Bsub)
          return current_rtlgraph.add(new Rconst(v1-v2,value_r,next_l));
        else
          return current_rtlgraph.add(new Rconst(v1*v2,value_r,next_l));
      }
      Mbinop mbinop;
      Register aux_r = new Register();
      Label aux_l;
      if(binop == Binop.Badd){
        if(expr1 instanceof Ecst){
          int v1 = ((Ecst)expr1).cst;
          aux_l = current_rtlgraph.add(new Rmunop(new Maddi(v1), value_r, next_l));
          return expr2.generate_rtl(value_r,aux_l);
        }
        else if(expr2 instanceof Ecst){
          int v2 = ((Ecst)expr2).cst;
          aux_l = current_rtlgraph.add(new Rmunop(new Maddi(v2), value_r, next_l));
          return expr1.generate_rtl(value_r,aux_l);
        }
        else
          mbinop = Mbinop.Madd;
      }
      else if(binop == Binop.Bsub){
        if(expr2 instanceof Ecst){
          int v2 = ((Ecst)expr2).cst;
          aux_l = current_rtlgraph.add(new Rmunop(new Maddi(-v2), value_r, next_l));
          return expr1.generate_rtl(value_r,aux_l);
        }
        else
          mbinop = Mbinop.Msub;
      }
      else if(binop == Binop.Bmul)
        mbinop = Mbinop.Mmul;
      else
        mbinop = Mbinop.Mdiv;
      
      aux_l = current_rtlgraph.add(new Rmbinop(mbinop, aux_r, value_r, next_l));
      aux_l = expr2.generate_rtl(aux_r,aux_l);
      return expr1.generate_rtl(value_r,aux_l);
    }
  }

  // generates the branchment rtl
  Label generate_rtl_c(Label true_l, Label false_l){
    if(binop == Binop.Band){
      return expr1.generate_rtl_c(expr2.generate_rtl_c(true_l,false_l),false_l);
    }
    else if(binop == Binop.Bor)
      return expr1.generate_rtl_c(true_l,expr2.generate_rtl_c(true_l,false_l));
    else if(binop == Binop.Blt || binop == Binop.Ble || binop == Binop.Bgt || binop == Binop.Bge || binop == Binop.Beq || binop == Binop.Bne){
      if(expr2 instanceof Ecst)
        return binop_cmp_cst_rtl_c(binop,expr1,(Ecst)expr2,true_l,false_l);
      else if(expr1 instanceof Ecst)
        return binop_cmp_cst_rtl_c(binop,(Ecst)expr1,expr2,true_l,false_l);
      else
        return binop_cmp_rtl_c(binop,expr1,expr2,true_l,false_l);
    }
    else
      return super.generate_rtl_c(true_l,false_l);
  }

  // changes the binary branchment into unary branchment when right hand side expression is constant
  Label binop_cmp_cst_rtl_c(Binop binop, Expr expr1, Ecst expr2, Label true_l, Label false_l){
    Register r = new Register();
    int cst = expr2.cst;
    Label aux_l;
    if(binop == Binop.Blt)
      aux_l = current_rtlgraph.add(new Rmubranch(new Mjli(cst),r,true_l,false_l));
    else if(binop == Binop.Ble)
      aux_l = current_rtlgraph.add(new Rmubranch(new Mjlei(cst),r,true_l,false_l));
    else if(binop == Binop.Bgt)
      aux_l = current_rtlgraph.add(new Rmubranch(new Mjgi(cst),r,true_l,false_l));
    else if(binop == Binop.Bge)
      aux_l = current_rtlgraph.add(new Rmubranch(new Mjgei(cst),r,true_l,false_l));
    else if(binop == Binop.Beq){
      aux_l = current_rtlgraph.add(new Rmubranch(new Mjz(),r,true_l,false_l));
      aux_l = current_rtlgraph.add(new Rmunop(new Maddi(-cst),r,aux_l));
    }
    else{
      aux_l = current_rtlgraph.add(new Rmubranch(new Mjnz(),r,true_l,false_l));
      aux_l = current_rtlgraph.add(new Rmunop(new Maddi(-cst),r,aux_l));
    }
    return expr1.generate_rtl(r,aux_l);
  }

  // if the constant is the on the left side just take change the order of expressions and the operation accordingly
  // and call binop_cmp_cst_rtl_c for the constant on the second argument
  Label binop_cmp_cst_rtl_c(Binop binop, Ecst expr1, Expr expr2, Label true_l, Label false_l){
    if(binop == Binop.Ble)
      binop = Binop.Bge;
    else if(binop == Binop.Bge)
      binop = Binop.Ble;
    else if(binop == Binop.Blt)
      binop = Binop.Bgt;
    else if(binop == Binop.Bgt)
      binop = Binop.Blt;
    return binop_cmp_cst_rtl_c(binop,expr2,expr1,true_l,false_l);
  }

  // genreates branchment for comparation binary operations
  Label binop_cmp_rtl_c(Binop binop, Expr expr1, Expr expr2, Label true_l, Label false_l){
    Register r1 = new Register();
    Register r2 = new Register();
    Label aux_l;
    if(binop == Binop.Blt)
      aux_l = current_rtlgraph.add(new Rmbbranch(Mbbranch.Mjl,r2,r1,true_l,false_l));
    else if(binop == Binop.Ble)
      aux_l = current_rtlgraph.add(new Rmbbranch(Mbbranch.Mjle,r2,r1,true_l,false_l));
    else if(binop == Binop.Bgt)
      aux_l = current_rtlgraph.add(new Rmbbranch(Mbbranch.Mjl,r1,r2,true_l,false_l));
    else if(binop == Binop.Bge)
      aux_l = current_rtlgraph.add(new Rmbbranch(Mbbranch.Mjle,r1,r2,true_l,false_l));
    else if(binop == Binop.Beq){
      aux_l = current_rtlgraph.add(new Rmubranch(new Mjz(),r1,true_l,false_l));
      aux_l = current_rtlgraph.add(new Rmbinop(Mbinop.Msub,r2,r1,aux_l));
    }
    else{
      aux_l = current_rtlgraph.add(new Rmubranch(new Mjz(),r1,false_l,true_l));
      aux_l = current_rtlgraph.add(new Rmbinop(Mbinop.Msub,r2,r1,aux_l));
    }
    aux_l = expr2.generate_rtl(r2,aux_l);
    return expr1.generate_rtl(r1,aux_l);
  }
}
