package mini_c;

import java.util.LinkedList;
import java.util.HashSet;
import java.util.Iterator;

class ToERTL implements RTLVisitor{
  private RTLgraph rtlgraph;
  private ERTLgraph ertlgraph;
  private ERTLfile ertlfile;
  private Label label;


  ERTLfile translate_file(RTLfile rtlfile){
    ertlfile = new ERTLfile();
    rtlfile.accept(this);
    return ertlfile;
  }

  private void translate_graph(Label l){
    if(ertlgraph.graph.containsKey(l))
      return;
    RTL r = rtlgraph.graph.get(l);
    if (r == null) return;
    label = l;
    r.accept(this);
    for (Label s: r.succ()) translate_graph(s);
  }

  public void visit(Rconst o) {
    ertlgraph.put(label,new ERconst(o.i,o.r,o.l));
  }
  public void visit(Raccess_global o) {
    ertlgraph.put(label,new ERaccess_global(o.s,o.r,o.l));
  }
  public void visit(Rassign_global o) {
    ertlgraph.put(label,new ERassign_global(o.r,o.s,o.l));
  }
  public void visit(Rload o) {
    ertlgraph.put(label,new ERload(o.r1,o.i,o.r2,o.l));
  }
  public void visit(Rstore o) {
    ertlgraph.put(label,new ERstore(o.r1,o.r2,o.i,o.l));
  }
  public void visit(Rmunop o) {
    ertlgraph.put(label,new ERmunop(o.m,o.r,o.l));
  }
  public void visit(Rmbinop o) {
    if(o.m == Mbinop.Mdiv){
      Label aux_l = ertlgraph.add(new ERmbinop(Mbinop.Mmov,Register.rax,o.r2,o.l));
      aux_l = ertlgraph.add(new ERmbinop(o.m,o.r1,Register.rax,aux_l));
      ertlgraph.put(label,new ERmbinop(Mbinop.Mmov,o.r2,Register.rax,aux_l));
    }
    else
      ertlgraph.put(label,new ERmbinop(o.m,o.r1,o.r2,o.l));
  }
  public void visit(Rmubranch o) {
    ertlgraph.put(label,new ERmubranch(o.m,o.r,o.l1,o.l2));
  }
  public void visit(Rmbbranch o) {
    ertlgraph.put(label,new ERmbbranch(o.m,o.r1,o.r2,o.l1,o.l2));
  }
  public void visit(Rcall o) {
    LinkedList<Register> aux_list_r = (LinkedList<Register>)o.rl;
    Iterator<Register> revit = aux_list_r.descendingIterator();
    int register_param_size = Register.parameters.size();
    Label aux_l = ertlgraph.add(new ERmbinop(Mbinop.Mmov,Register.result,o.r,o.l));
    aux_l = ertlgraph.add(new ERcall(o.s,aux_list_r.size(),aux_l));
    Register aux_r;
    int i = aux_list_r.size()-1;
    while(revit.hasNext()){
      aux_r = revit.next();
      if(i < register_param_size)
        aux_l = ertlgraph.add(new ERmbinop(Mbinop.Mmov,aux_r,Register.parameters.get(i),aux_l));
      else
        aux_l = ertlgraph.add(new ERset_param(aux_r,i-register_param_size,aux_l));
      i--;
    }
    ertlgraph.put(label,new ERgoto(aux_l));
  }
  public void visit(Rgoto o) {
    ertlgraph.put(label,new ERgoto(o.l));
  }
  public void visit(RTLfun o) {
    ERTLfun ertlfun = new ERTLfun(o.name,o.formals.size());
    ertlgraph = new ERTLgraph();
    // load parameters from registers
    Iterator<Register> revit_formals = ((LinkedList<Register>)o.formals).descendingIterator();
    int register_param_size = Register.parameters.size();
    int i = o.formals.size()-1;
    Register aux_r;
    Label aux_l = o.entry;
    while(revit_formals.hasNext()){
      aux_r = revit_formals.next();
      if(i < register_param_size)
        aux_l = ertlgraph.add(new ERmbinop(Mbinop.Mmov,Register.parameters.get(i),aux_r,aux_l));
      else
        aux_l = ertlgraph.add(new ERget_param(i-register_param_size,aux_r,aux_l));
      i--;
    }
    // save callee_saved in fresh registers
    LinkedList<Register> list_callee_saved_r = new LinkedList<Register>();
    for(Register r : Register.callee_saved)
      list_callee_saved_r.add(new Register());
    Iterator<Register> revit_callee = ((LinkedList<Register>)Register.callee_saved).descendingIterator();
    i = list_callee_saved_r.size()-1;
    while(revit_callee.hasNext()){
      aux_l = ertlgraph.add(new ERmbinop(Mbinop.Mmov,revit_callee.next(),list_callee_saved_r.get(i),aux_l));
      i--;
    }
    // alloc activation table
    ertlfun.entry = ertlgraph.add(new ERalloc_frame(aux_l));
    ertlfun.locals = new HashSet<Register>(o.locals);
    ertlfun.locals.addAll(list_callee_saved_r);
    //translate instructions
    rtlgraph = o.body;
    translate_graph(o.entry);
    // return instruction
    aux_l = ertlgraph.add(new ERreturn());
    // desalloc activation table
    aux_l = ertlgraph.add(new ERdelete_frame(aux_l));
    // restore callee_saved registers
    revit_callee = ((LinkedList<Register>)Register.callee_saved).descendingIterator();
    i = list_callee_saved_r.size()-1;
    while(revit_callee.hasNext()){
      aux_l = ertlgraph.add(new ERmbinop(Mbinop.Mmov,list_callee_saved_r.get(i),revit_callee.next(),aux_l));
      i--;
    }
    // copy result into rax
    ertlgraph.put(o.exit,new ERmbinop(Mbinop.Mmov,o.result,Register.result,aux_l));
    ertlfun.body = ertlgraph;
    ertlfile.funs.add(ertlfun);
  }
  public void visit(RTLfile o) {
    ertlfile.gvars = new LinkedList<String>(o.gvars);

    ERTLfun ertlfun;
    for(RTLfun rtlfun : o.funs)
      rtlfun.accept(this);
  }
}