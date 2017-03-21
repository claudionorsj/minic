package mini_c;

import java.util.LinkedList;

class ToLTL implements ERTLVisitor{
  private Coloring coloring;
  private int activation_table_size;
  private ERTLgraph ertlgraph;
  private LTLgraph ltlgraph;
  private LTLfile ltlfile;
  private Label label;


  LTLfile translate_file(ERTLfile ertlfile){
    ltlfile = new LTLfile();
    ertlfile.accept(this);
    return ltlfile;
  }

  private void translate_graph(Label l){
    if(ltlgraph.graph.containsKey(l))
      return;
    ERTL er = ertlgraph.graph.get(l);
    if (er == null) return;
    label = l;
    er.accept(this);
    for (Label s: er.succ()) translate_graph(s);
  }

  public void visit(ERconst o) {
    ltlgraph.put(label,new Lconst(o.i,get_operand(o.r),o.l));
  }
  public void visit(ERaccess_global o) {
    Operand op = get_operand(o.r);
    if(op instanceof Reg)
      ltlgraph.put(label,new Laccess_global(o.s,((Reg)op).r,o.l));
    else{
      Label aux_l = ltlgraph.add(new Lmbinop(Mbinop.Mmov,new Reg(Register.tmp1),op,o.l));
      ltlgraph.put(label,new Laccess_global(o.s,Register.tmp1,aux_l));
    }
  }
  public void visit(ERassign_global o) {
    Operand op = get_operand(o.r);
    if(op instanceof Reg)
      ltlgraph.put(label,new Lassign_global(((Reg)op).r,o.s,o.l));
    else{
      Label aux_l = ltlgraph.add(new Lassign_global(Register.tmp1,o.s,o.l));
      ltlgraph.put(label,new Lmbinop(Mbinop.Mmov,op,new Reg(Register.tmp1),aux_l));
    }
  }
  public void visit(ERload o) {
    Operand op1 = get_operand(o.r1);
    Operand op2 = get_operand(o.r2);
    if(op1 instanceof Reg){
      if(op2 instanceof Reg){
        ltlgraph.put(label,new Lload(((Reg)op1).r,o.i,((Reg)op2).r,o.l));
      }
      else{
        Label aux_l = ltlgraph.add(new Lmbinop(Mbinop.Mmov,new Reg(Register.tmp2),op2,o.l));
        ltlgraph.put(label,new Lload(((Reg)op1).r,o.i,Register.tmp2,aux_l));
      }
    }
    else{
      if(op2 instanceof Reg){
        Label aux_l = ltlgraph.add(new Lload(Register.tmp1,o.i,((Reg)op2).r,o.l));
        ltlgraph.put(label,new Lmbinop(Mbinop.Mmov,op1,new Reg(Register.tmp1),aux_l));
      }
      else{
        Label aux_l = ltlgraph.add(new Lmbinop(Mbinop.Mmov,new Reg(Register.tmp2),op2,o.l));
        aux_l = ltlgraph.add(new Lload(Register.tmp1,o.i,Register.tmp2,aux_l));
        ltlgraph.put(label,new Lmbinop(Mbinop.Mmov,op1,new Reg(Register.tmp1),aux_l));
      }
    }
  }
  public void visit(ERstore o) {
    Operand op1 = get_operand(o.r1);
    Operand op2 = get_operand(o.r2);
    if(op1 instanceof Reg){
      if(op2 instanceof Reg){
        ltlgraph.put(label,new Lstore(((Reg)op1).r,((Reg)op2).r,o.i,o.l));
      }
      else{
        Label aux_l = ltlgraph.add(new Lmbinop(Mbinop.Mmov,new Reg(Register.tmp2),op2,o.l));
        ltlgraph.put(label,new Lstore(((Reg)op1).r,Register.tmp2,o.i,aux_l));
      }
    }
    else{
      if(op2 instanceof Reg){
        Label aux_l = ltlgraph.add(new Lstore(Register.tmp1,((Reg)op2).r,o.i,o.l));
        ltlgraph.put(label,new Lmbinop(Mbinop.Mmov,op1,new Reg(Register.tmp1),aux_l));
      }
      else{
        Label aux_l = ltlgraph.add(new Lmbinop(Mbinop.Mmov,new Reg(Register.tmp2),op2,o.l));
        aux_l = ltlgraph.add(new Lstore(Register.tmp1,Register.tmp2,o.i,aux_l));
        ltlgraph.put(label,new Lmbinop(Mbinop.Mmov,op1,new Reg(Register.tmp1),aux_l));
      }
    }
  }
  public void visit(ERmunop o) {
    ltlgraph.put(label,new Lmunop(o.m,get_operand(o.r),o.l));
  }
  public void visit(ERmbinop o) {
    Operand op1 = get_operand(o.r1);
    Operand op2 = get_operand(o.r2);
    if(o.m == Mbinop.Mmov && op1.equals(op2))
      ltlgraph.put(label,new Lgoto(o.l));
    else if(o.m == Mbinop.Mmul && (!(op1 instanceof Reg) || !(op2 instanceof Reg))){
      if(!(op1 instanceof Reg) && !(op2 instanceof Reg)){
        Label aux_l = ltlgraph.add(new Lmbinop(Mbinop.Mmov,new Reg(Register.tmp2),op2,o.l));
        aux_l = ltlgraph.add(new Lmbinop(o.m,new Reg(Register.tmp1),new Reg(Register.tmp2),aux_l));
        aux_l = ltlgraph.add(new Lmbinop(Mbinop.Mmov,op2,new Reg(Register.tmp2),aux_l));
        ltlgraph.put(label,new Lmbinop(Mbinop.Mmov,op1,new Reg(Register.tmp1),aux_l));
      }
      else if(!(op1 instanceof Reg)){
        Label aux_l = ltlgraph.add(new Lmbinop(o.m,new Reg(Register.tmp1),op2,o.l));
        ltlgraph.put(label,new Lmbinop(Mbinop.Mmov,op1,new Reg(Register.tmp1),aux_l));
      }
      else{
        Label aux_l = ltlgraph.add(new Lmbinop(Mbinop.Mmov,new Reg(Register.tmp2),op2,o.l));
        aux_l = ltlgraph.add(new Lmbinop(o.m,op1,new Reg(Register.tmp2),aux_l));
        ltlgraph.put(label,new Lmbinop(Mbinop.Mmov,op2,new Reg(Register.tmp2),aux_l));
      }
    }
    else if(!(op1 instanceof Reg) && !(op2 instanceof Reg)){
      Label aux_l = ltlgraph.add(new Lmbinop(o.m,new Reg(Register.tmp1),op2,o.l));
      ltlgraph.put(label,new Lmbinop(Mbinop.Mmov,op1,new Reg(Register.tmp1),aux_l));
    }
    else
      ltlgraph.put(label,new Lmbinop(o.m,op1,op2,o.l));
  }
  public void visit(ERmubranch o) {
    Operand op = get_operand(o.r);
    if(op instanceof Reg)
      ltlgraph.put(label,new Lmubranch(o.m,((Reg)op).r,o.l1,o.l2));
    else{
      Label aux_l = ltlgraph.add(new Lmubranch(o.m,Register.tmp1,o.l1,o.l2));
      ltlgraph.put(label,new Lmbinop(Mbinop.Mmov,op,new Reg(Register.tmp1),aux_l));
    }
  }
  public void visit(ERmbbranch o) {
    Operand op1 = get_operand(o.r1);
    Operand op2 = get_operand(o.r2);
    if(op1 instanceof Reg){
      if(op2 instanceof Reg){
        ltlgraph.put(label,new Lmbbranch(o.m,((Reg)op1).r,((Reg)op2).r,o.l1,o.l2));
      }
      else{
        Label aux_l = ltlgraph.add(new Lmbbranch(o.m,((Reg)op1).r,Register.tmp2,o.l1,o.l2));
        ltlgraph.put(label,new Lmbinop(Mbinop.Mmov,op2,new Reg(Register.tmp2),aux_l));
      }
    }
    else{
      if(op2 instanceof Reg){
        Label aux_l = ltlgraph.add(new Lmbbranch(o.m,Register.tmp1,((Reg)op2).r,o.l1,o.l2));
        ltlgraph.put(label,new Lmbinop(Mbinop.Mmov,op1,new Reg(Register.tmp1),aux_l));
      }
      else{
        Label aux_l = ltlgraph.add(new Lmbbranch(o.m,Register.tmp1,Register.tmp2,o.l1,o.l2));
        aux_l = ltlgraph.add(new Lmbinop(Mbinop.Mmov,op2,new Reg(Register.tmp2),aux_l));
        ltlgraph.put(label,new Lmbinop(Mbinop.Mmov,op1,new Reg(Register.tmp1),aux_l));
      }
    }
  }
  public void visit(ERgoto o) {
    ltlgraph.put(label,new Lgoto(o.l));
  }
  public void visit(ERcall o) {
    ltlgraph.put(label,new Lcall(o.s,o.l));
  }
  public void visit(ERalloc_frame o) {
    int k = 8*coloring.nlocals;
    if(k == 0)
      ltlgraph.put(label,new Lgoto(o.l));
    else
      ltlgraph.put(label,new Lmunop(new Maddi(-k), new Reg(Register.rsp), o.l));
  }
  public void visit(ERdelete_frame o) {
    int k = 8*coloring.nlocals;
    if(k == 0)
      ltlgraph.put(label,new Lgoto(o.l));
    else
      ltlgraph.put(label,new Lmunop(new Maddi(k), new Reg(Register.rsp), o.l));
  }
  public void visit(ERget_param o) {
    Operand op = get_operand(o.r);
    int new_k = activation_table_size + 8*o.i;
    if(op instanceof Reg)
      ltlgraph.put(label,new Lload(Register.rsp,new_k,((Reg)op).r,o.l));
    else{
      Label aux_l = ltlgraph.add(new Lmbinop(Mbinop.Mmov,new Reg(Register.tmp1),op,o.l));
      ltlgraph.put(label,new Lload(Register.rsp,new_k,Register.tmp1,aux_l));
    }
  }
  public void visit(ERset_param o) {
    Operand op = get_operand(o.r);
    int new_k = activation_table_size + 8*o.n;
    if(op instanceof Reg)
      ltlgraph.put(label,new Lstore(((Reg)op).r,Register.rsp,new_k,o.l));
    else{
      Label aux_l = ltlgraph.add(new Lstore(Register.tmp1,Register.rsp,new_k,o.l));
      ltlgraph.put(label,new Lmbinop(Mbinop.Mmov,op,new Reg(Register.tmp1),aux_l));
    }
  }
  public void visit(ERreturn o) {
    ltlgraph.put(label,new Lreturn());
  }
  public void visit(ERTLfun o) {
    // prepare coloring for ertlfun
    Liveness lness = new Liveness(o.body);
    Interference intf = new Interference(lness);
    coloring = new Coloring(intf);

    // create new graph for the function
    ltlgraph = new LTLgraph();

    // create a new function
    LTLfun ltlfun = new LTLfun(o.name);

    // pre calculating size of activation table
    activation_table_size = 8*(1+Math.max(0,o.formals-6)+coloring.nlocals);


    ltlfun.entry = o.entry;
    ertlgraph = o.body;
    translate_graph(ltlfun.entry);
    ltlfun.body = ltlgraph;
    ltlfile.funs.add(ltlfun);

    // lness.print(ertlfun.entry);
    // System.out.println();
    // System.out.println();
    // intf.print();
    // System.out.println();
    // System.out.println();
    // coloring.print();
    // System.out.println();
    // System.out.println();
    // ltlfun.print();
    // System.out.println();
    // System.out.println();
  }
  public void visit(ERTLfile o) {
    ltlfile.gvars = new LinkedList<String>(o.gvars);

    for(ERTLfun ertlfun : o.funs)
      ertlfun.accept(this);
  }

  Operand get_operand(Register r){
    if(coloring.colors.containsKey(r))
      return coloring.colors.get(r);
    return new Reg(r);
  }
}