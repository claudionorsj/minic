package mini_c;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

class File extends Base{
  final LinkedList<Decl> list_decl;

  File(LinkedList<Decl> list_decl) {
    super();
    this.list_decl = list_decl;
  }

  void semantic_analysis(LinkedList<String> errors){
    // add global context
    list_context.add(new HashMap<String,String>());

    // add int to the set of types
    set_types.add("int");

    // add putchar and sbrk to the map of functions and to the global context
    LinkedList<Param> list_param = new LinkedList<Param>();
    list_param.add(new Param("int","n"));
    map_fcts_params.put("putchar", list_param);
    map_fcts_params.put("sbrk", list_param);
    list_context.get(0).put("putchar","int");
    list_context.get(0).put("sbrk","void");

    // store global variables
    map_fcts_locals.put("",new HashSet<String>());

    // check for semantic errors in each declaration
    for(Decl decl : list_decl){
      current_fct = "";
      decl.semantic_analysis(errors);
    }
  }

  RTLfile generate_rtl(){
    for(Decl decl : list_decl)
      decl.generate_rtl();
    
    return rtlfile;
  }

  ERTLfile generate_ertl(){
    ertlfile.gvars = new LinkedList<String>(rtlfile.gvars);

    ERTLfun ertlfun;
    for(RTLfun rtlfun : rtlfile.funs){
      current_ertlgraph = new ERTLgraph();
      ertlfun = new ERTLfun(rtlfun.name,rtlfun.formals.size());
      // load parameters from registers
      Iterator<Register> revit_formals = ((LinkedList<Register>)rtlfun.formals).descendingIterator();
      int register_param_size = Register.parameters.size();
      int i = rtlfun.formals.size()-1;
      Register aux_r;
      Label aux_l = rtlfun.entry;
      while(revit_formals.hasNext()){
        aux_r = revit_formals.next();
        if(i < register_param_size)
          aux_l = current_ertlgraph.add(new ERmbinop(Mbinop.Mmov,Register.parameters.get(i),aux_r,aux_l));
        else
          aux_l = current_ertlgraph.add(new ERget_param(i-register_param_size,aux_r,aux_l));
        i--;
      }
      // save callee_saved in fresh registers
      LinkedList<Register> list_callee_saved_r = new LinkedList<Register>();
      for(Register r : Register.callee_saved)
        list_callee_saved_r.add(new Register());
      Iterator<Register> revit_callee = ((LinkedList<Register>)Register.callee_saved).descendingIterator();
      i = list_callee_saved_r.size()-1;
      while(revit_callee.hasNext()){
        aux_l = current_ertlgraph.add(new ERmbinop(Mbinop.Mmov,revit_callee.next(),list_callee_saved_r.get(i),aux_l));
        i--;
      }
      // alloc activation table
      ertlfun.entry = current_ertlgraph.add(new ERalloc_frame(aux_l));
      ertlfun.locals = new HashSet<Register>(rtlfun.locals);
      ertlfun.locals.addAll(list_callee_saved_r);
      translate_graph_rtl_to_ertl(rtlfun.body);
      // return instruction
      aux_l = current_ertlgraph.add(new ERreturn());
      // desalloc activation table
      aux_l = current_ertlgraph.add(new ERdelete_frame(aux_l));
      // restore callee_saved registers
      revit_callee = ((LinkedList<Register>)Register.callee_saved).descendingIterator();
      i = list_callee_saved_r.size()-1;
      while(revit_callee.hasNext()){
        aux_l = current_ertlgraph.add(new ERmbinop(Mbinop.Mmov,list_callee_saved_r.get(i),revit_callee.next(),aux_l));
        i--;
      }
      // copy result into rax
      current_ertlgraph.put(rtlfun.exit,new ERmbinop(Mbinop.Mmov,rtlfun.result,Register.result,aux_l));
      ertlfun.body = current_ertlgraph;
      ertlfile.funs.add(ertlfun);
    }
    
    return ertlfile;
  }

  void translate_graph_rtl_to_ertl(RTLgraph rtlgraph){
    for(HashMap.Entry<Label,RTL> entry_l_rtl : rtlgraph.graph.entrySet())
      translate_inst_rtl_to_ertl(entry_l_rtl.getKey(),entry_l_rtl.getValue());
  }

  void translate_inst_rtl_to_ertl(Label l,RTL r){
    if(r instanceof Rconst){
      Rconst aux = (Rconst)r;
      current_ertlgraph.put(l,new ERconst(aux.i,aux.r,aux.l));
    }
    else if(r instanceof Raccess_global){
      Raccess_global aux = (Raccess_global)r;
      current_ertlgraph.put(l,new ERaccess_global(aux.s,aux.r,aux.l));
    }
    else if(r instanceof Rassign_global){
      Rassign_global aux = (Rassign_global)r;
      current_ertlgraph.put(l,new ERassign_global(aux.r,aux.s,aux.l));
    }
    else if(r instanceof Rload){
      Rload aux = (Rload)r;
      current_ertlgraph.put(l,new ERload(aux.r1,aux.i,aux.r2,aux.l));
    }
    else if(r instanceof Rstore){
      Rstore aux = (Rstore)r;
      current_ertlgraph.put(l,new ERstore(aux.r1,aux.r2,aux.i,aux.l));
    }
    else if(r instanceof Rmunop){
      Rmunop aux = (Rmunop)r;
      current_ertlgraph.put(l,new ERmunop(aux.m,aux.r,aux.l));
    }
    else if(r instanceof Rmbinop){
      Rmbinop aux = (Rmbinop)r;
      if(aux.m == Mbinop.Mdiv){
        Label aux_l = current_ertlgraph.add(new ERmbinop(Mbinop.Mmov,Register.rax,aux.r2,aux.l));
        aux_l = current_ertlgraph.add(new ERmbinop(aux.m,aux.r1,Register.rax,aux_l));
        aux_l = current_ertlgraph.add(new ERconst(0,Register.rdx,aux_l));
        aux_l = current_ertlgraph.add(new ERmbinop(Mbinop.Mmov,Register.rdx,Register.rbx,aux_l));
        current_ertlgraph.put(l,new ERmbinop(Mbinop.Mmov,aux.r2,Register.rax,aux_l));
      }
      else
        current_ertlgraph.put(l,new ERmbinop(aux.m,aux.r1,aux.r2,aux.l));
    }
    else if(r instanceof Rmubranch){
      Rmubranch aux = (Rmubranch)r;
      current_ertlgraph.put(l,new ERmubranch(aux.m,aux.r,aux.l1,aux.l2));
    }
    else if(r instanceof Rmbbranch){
      Rmbbranch aux = (Rmbbranch)r;
      current_ertlgraph.put(l,new ERmbbranch(aux.m,aux.r1,aux.r2,aux.l1,aux.l2));
    }
    else if(r instanceof Rgoto){
      Rgoto aux = (Rgoto)r;
      current_ertlgraph.put(l,new ERgoto(aux.l));
    }
    else{
      Rcall aux = (Rcall)r;
      LinkedList<Register> aux_list_r = (LinkedList<Register>)aux.rl;
      Iterator<Register> revit = aux_list_r.descendingIterator();
      int register_param_size = Register.parameters.size();
      Label aux_l = current_ertlgraph.add(new ERmbinop(Mbinop.Mmov,Register.result,aux.r,aux.l));
      aux_l = current_ertlgraph.add(new ERcall(aux.s,aux_list_r.size(),aux_l));
      Register aux_r;
      int i = aux_list_r.size()-1;
      while(revit.hasNext()){
        aux_r = revit.next();
        if(i < register_param_size)
          aux_l = current_ertlgraph.add(new ERmbinop(Mbinop.Mmov,aux_r,Register.parameters.get(i),aux_l));
        else
          aux_l = current_ertlgraph.add(new ERset_param(aux_r,i-register_param_size,aux_l));
        i--;
      }
      current_ertlgraph.put(l,new ERgoto(aux_l));
    }
  }

  LTLfile generate_ltl(){
    ltlfile.gvars = new LinkedList<String>(ertlfile.gvars);

    LTLfun ltlfun;
    for(ERTLfun ertlfun : ertlfile.funs){
      Liveness lness = new Liveness(ertlfun.body);
      Interference intf = new Interference(lness);
      current_color = new Coloring(intf);
      current_ltlgraph = new LTLgraph();
      ltlfun = new LTLfun(ertlfun.name);
      ltlfun.entry = ertlfun.entry;
      activation_table_size = 8*(1+Math.max(0,ertlfun.formals-6)+current_color.nlocals);
      translate_graph_ertl_to_lrtl(ertlfun.body);
      ltlfun.body = current_ltlgraph;
      ltlfile.funs.add(ltlfun);
      // rtlfile.print();
      // System.out.println();
      // System.out.println();
      // lness.print(ertlfun.entry);
      // System.out.println();
      // System.out.println();
      // intf.print();
      // System.out.println();
      // System.out.println();
      // current_color.print();
      // System.out.println();
      // System.out.println();
      // ltlfun.print();
      // System.out.println();
      // System.out.println();
    }

    return ltlfile;
  }

  void translate_graph_ertl_to_lrtl(ERTLgraph ertlgraph){
    for(HashMap.Entry<Label,ERTL> entry_l_ertl : ertlgraph.graph.entrySet())
      translate_inst_ertl_to_ltl(entry_l_ertl.getKey(),entry_l_ertl.getValue());
  }

  void translate_inst_ertl_to_ltl(Label l, ERTL er){
    Label aux_l;
    if(er instanceof ERcall){
      ERcall aux = (ERcall)er;
      current_ltlgraph.put(l,new Lcall(aux.s,aux.l));
    }
    else if(er instanceof ERgoto){
      ERgoto aux = (ERgoto)er;
      current_ltlgraph.put(l,new Lgoto(aux.l));
    }
    else if(er instanceof ERreturn){
      ERreturn aux = (ERreturn)er;
      current_ltlgraph.put(l,new Lreturn());
    }
    else if(er instanceof ERaccess_global){
      ERaccess_global aux = (ERaccess_global)er;
      Operand op = get_operand(aux.r);
      if(op instanceof Reg)
        current_ltlgraph.put(l,new Laccess_global(aux.s,((Reg)op).r,aux.l));
      else{
        aux_l = current_ltlgraph.add(new Lmbinop(Mbinop.Mmov,new Reg(Register.tmp1),op,aux.l));
        current_ltlgraph.put(l,new Laccess_global(aux.s,Register.tmp1,aux_l));
      }
    }
    else if(er instanceof ERassign_global){
      ERassign_global aux = (ERassign_global)er;
      Operand op = get_operand(aux.r);
      if(op instanceof Reg)
        current_ltlgraph.put(l,new Lassign_global(((Reg)op).r,aux.s,aux.l));
      else{
        aux_l = current_ltlgraph.add(new Lassign_global(Register.tmp1,aux.s,aux.l));
        current_ltlgraph.put(l,new Lmbinop(Mbinop.Mmov,op,new Reg(Register.tmp1),aux_l));
      }
    }
    else if(er instanceof ERload){
      ERload aux = (ERload)er;
      Operand op1 = get_operand(aux.r1);
      Operand op2 = get_operand(aux.r2);
      if(op1 instanceof Reg){
        if(op2 instanceof Reg){
          current_ltlgraph.put(l,new Lload(((Reg)op1).r,aux.i,((Reg)op2).r,aux.l));
        }
        else{
          aux_l = current_ltlgraph.add(new Lmbinop(Mbinop.Mmov,new Reg(Register.tmp2),op2,aux.l));
          current_ltlgraph.put(l,new Lload(((Reg)op1).r,aux.i,Register.tmp2,aux_l));
        }
      }
      else{
        if(op2 instanceof Reg){
          aux_l = current_ltlgraph.add(new Lload(Register.tmp1,aux.i,((Reg)op2).r,aux.l));
          current_ltlgraph.put(l,new Lmbinop(Mbinop.Mmov,op1,new Reg(Register.tmp1),aux_l));
        }
        else{
          aux_l = current_ltlgraph.add(new Lmbinop(Mbinop.Mmov,new Reg(Register.tmp2),op2,aux.l));
          aux_l = current_ltlgraph.add(new Lload(Register.tmp1,aux.i,Register.tmp2,aux_l));
          current_ltlgraph.put(l,new Lmbinop(Mbinop.Mmov,op1,new Reg(Register.tmp1),aux_l));
        }
      }
    }
    else if(er instanceof ERstore){
      ERstore aux = (ERstore)er;
      Operand op1 = get_operand(aux.r1);
      Operand op2 = get_operand(aux.r2);
      if(op1 instanceof Reg){
        if(op2 instanceof Reg){
          current_ltlgraph.put(l,new Lstore(((Reg)op1).r,((Reg)op2).r,aux.i,aux.l));
        }
        else{
          aux_l = current_ltlgraph.add(new Lmbinop(Mbinop.Mmov,new Reg(Register.tmp2),op2,aux.l));
          current_ltlgraph.put(l,new Lstore(((Reg)op1).r,Register.tmp2,aux.i,aux_l));
        }
      }
      else{
        if(op2 instanceof Reg){
          aux_l = current_ltlgraph.add(new Lstore(Register.tmp1,((Reg)op2).r,aux.i,aux.l));
          current_ltlgraph.put(l,new Lmbinop(Mbinop.Mmov,op1,new Reg(Register.tmp1),aux_l));
        }
        else{
          aux_l = current_ltlgraph.add(new Lmbinop(Mbinop.Mmov,new Reg(Register.tmp2),op2,aux.l));
          aux_l = current_ltlgraph.add(new Lstore(Register.tmp1,Register.tmp2,aux.i,aux_l));
          current_ltlgraph.put(l,new Lmbinop(Mbinop.Mmov,op1,new Reg(Register.tmp1),aux_l));
        }
      }
    }
    else if(er instanceof ERmubranch){
      ERmubranch aux = (ERmubranch)er;
      Operand op = get_operand(aux.r);
      if(op instanceof Reg)
        current_ltlgraph.put(l,new Lmubranch(aux.m,((Reg)op).r,aux.l1,aux.l2));
      else{
        aux_l = current_ltlgraph.add(new Lmubranch(aux.m,Register.tmp1,aux.l1,aux.l2));
        current_ltlgraph.put(l,new Lmbinop(Mbinop.Mmov,op,new Reg(Register.tmp1),aux_l));
      }
    }
    else if(er instanceof ERmbbranch){
      ERmbbranch aux = (ERmbbranch)er;
      Operand op1 = get_operand(aux.r1);
      Operand op2 = get_operand(aux.r2);
      if(op1 instanceof Reg){
        if(op2 instanceof Reg){
          current_ltlgraph.put(l,new Lmbbranch(aux.m,((Reg)op1).r,((Reg)op2).r,aux.l1,aux.l2));
        }
        else{
          aux_l = current_ltlgraph.add(new Lmbbranch(aux.m,((Reg)op1).r,Register.tmp2,aux.l1,aux.l2));
          current_ltlgraph.put(l,new Lmbinop(Mbinop.Mmov,op2,new Reg(Register.tmp2),aux_l));
        }
      }
      else{
        if(op2 instanceof Reg){
          aux_l = current_ltlgraph.add(new Lmbbranch(aux.m,Register.tmp1,((Reg)op2).r,aux.l1,aux.l2));
          current_ltlgraph.put(l,new Lmbinop(Mbinop.Mmov,op1,new Reg(Register.tmp1),aux_l));
        }
        else{
          aux_l = current_ltlgraph.add(new Lmbbranch(aux.m,Register.tmp1,Register.tmp2,aux.l1,aux.l2));
          aux_l = current_ltlgraph.add(new Lmbinop(Mbinop.Mmov,op2,new Reg(Register.tmp2),aux_l));
          current_ltlgraph.put(l,new Lmbinop(Mbinop.Mmov,op1,new Reg(Register.tmp1),aux_l));
        }
      }
    }
    else if(er instanceof ERconst){
      ERconst aux = (ERconst)er;
      current_ltlgraph.put(l,new Lconst(aux.i,get_operand(aux.r),aux.l));
    }
    else if(er instanceof ERmunop){
      ERmunop aux = (ERmunop)er;
      current_ltlgraph.put(l,new Lmunop(aux.m,get_operand(aux.r),aux.l));
    }
    else if(er instanceof ERmbinop){
      ERmbinop aux = (ERmbinop)er;
      Operand op1 = get_operand(aux.r1);
      Operand op2 = get_operand(aux.r2);
      if(aux.m == Mbinop.Mmov && op1.equals(op2))
        current_ltlgraph.put(l,new Lgoto(aux.l));
      else if(!(op1 instanceof Reg) && !(op2 instanceof Reg)){
        aux_l = current_ltlgraph.add(new Lmbinop(aux.m,new Reg(Register.tmp1),new Reg(Register.tmp2),aux.l));
        current_ltlgraph.put(l,new Lmbinop(Mbinop.Mmov,op1,new Reg(Register.tmp1),aux_l));
      }
      else
        current_ltlgraph.put(l,new Lmbinop(aux.m,op1,op2,aux.l));
    }
    else if(er instanceof ERalloc_frame){
      ERalloc_frame aux = (ERalloc_frame)er;
      int k = 8*current_color.nlocals;
      if(k == 0)
        current_ltlgraph.put(l,new Lgoto(aux.l));
      else
        current_ltlgraph.put(l,new Lmunop(new Maddi(-k), new Reg(Register.rsp), aux.l));
    }
    else if(er instanceof ERdelete_frame){
      ERdelete_frame aux = (ERdelete_frame)er;
      int k = 8*current_color.nlocals;
      if(k == 0)
        current_ltlgraph.put(l,new Lgoto(aux.l));
      else
        current_ltlgraph.put(l,new Lmunop(new Maddi(k), new Reg(Register.rsp), aux.l));
    }
    else if(er instanceof ERset_param){
      ERget_param aux = (ERget_param)er;
      Operand op = get_operand(aux.r);
      int new_k = activation_table_size + aux.i;
      if(op instanceof Reg){
        current_ltlgraph.put(l,new Lstore(((Reg)op).r,Register.rsp,new_k,aux.l));
      }
      else{
        aux_l = current_ltlgraph.add(new Lstore(Register.tmp1,Register.rsp,new_k,aux.l));
        current_ltlgraph.put(l,new Lmbinop(Mbinop.Mmov,op,new Reg(Register.tmp1),aux_l));
      }
    }
    else if(er instanceof ERget_param){
      ERget_param aux = (ERget_param)er;
      Operand op = get_operand(aux.r);
      int new_k = activation_table_size + aux.i;
      if(op instanceof Reg){
        current_ltlgraph.put(l,new Lload(Register.rsp,new_k,((Reg)op).r,aux.l));
      }
      else{
        aux_l = current_ltlgraph.add(new Lmbinop(Mbinop.Mmov,new Reg(Register.tmp1),op,aux.l));
        current_ltlgraph.put(l,new Lload(Register.rsp,new_k,Register.tmp1,aux_l));
      }
    }
  }

  Operand get_operand(Register r){
    if(current_color.colors.containsKey(r))
      return current_color.colors.get(r);
    else
      return new Reg(r);
  }
}
