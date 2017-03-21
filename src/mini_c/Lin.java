package mini_c;

import java.util.HashSet;

class Lin implements LTLVisitor {
  LTLfile ltlfile;
  private LTLgraph cfg;
  private X86_64 asm;
  private HashSet<Label> visited;
  String s;

  Lin(LTLfile ltlfile, X86_64 asm, String s){
    this.ltlfile = ltlfile;
    this.asm = asm;
    this.s = s;
    visited = new HashSet<Label>();
  }

  void translate(){
    asm.globl("main");
    for(String glob : ltlfile.gvars)
       asm.dlabel(glob).quad(0);
    for(LTLfun ltlfun : ltlfile.funs){
      cfg = ltlfun.body;
      asm.label(ltlfun.name);
      lin(ltlfun.entry);
    }
    asm.printToFile(s);
  }

  private void lin(Label l) {
    if(visited.contains(l)){
      asm.needLabel(l);
      asm.jmp(l.name);
    }
    else{
      visited.add(l);
      asm.label(l);
      cfg.graph.get(l).accept(this);
    }
  }

  public void visit(Laccess_global o) {
    asm.movq(o.s,o.r.toString());
    lin(o.l);
  }
  public void visit(Lassign_global o) {
    asm.movq(o.r.toString(),o.s);
    lin(o.l); 
  }
  public void visit(Lload o) {
    asm.movq(8*o.i+"("+o.r1+")",o.r2.toString());
    lin(o.l);
  }
  public void visit(Lstore o) {
    asm.movq(o.r1.toString(),8*o.i+"("+o.r2+")");
    lin(o.l);
  }
  public void visit(Lmubranch o) {
    if(visited.contains(o.l1) && visited.contains(o.l2)){
      asm.needLabel(o.l1);
      jcc(o.m,o.r.name,o.l1,false);
      lin(o.l2);
    }
    else if(visited.contains(o.l2)){
      asm.needLabel(o.l2);
      jcc(o.m,o.r.name,o.l2,true);
      lin(o.l1);
    }
    else if(visited.contains(o.l1)){
      asm.needLabel(o.l1);
      jcc(o.m,o.r.name,o.l1,false);
      lin(o.l2);
    }
    else{
      asm.needLabel(o.l1);
      jcc(o.m,o.r.name,o.l1,false);
      lin(o.l2);
      if(!visited.contains(o.l1))
        lin(o.l1);
    }
  }
  public void visit(Lmbbranch o) {
    if(visited.contains(o.l1) && visited.contains(o.l2)){
      asm.needLabel(o.l1);
      jcc(o.m,o.r1.name,o.r2.name,o.l1,false);
      lin(o.l2);
    }
    else if(visited.contains(o.l2)){
      asm.needLabel(o.l2);
      jcc(o.m,o.r1.name,o.r2.name,o.l2,true);
      lin(o.l1);
    }
    else if(visited.contains(o.l1)){
      asm.needLabel(o.l1);
      jcc(o.m,o.r1.name,o.r2.name,o.l1,false);
      lin(o.l2);
    }
    else{
      asm.needLabel(o.l1);
      jcc(o.m,o.r1.name,o.r2.name,o.l1,false);
      lin(o.l2);
      if(!visited.contains(o.l1))
        lin(o.l1);
    }
  }
  public void visit(Lgoto o) {
    lin(o.l);
  }
  public void visit(Lreturn o) {
    asm.ret();
  }
  public void visit(Lconst o) {
    asm.movq(o.i,o.o.toString());
    lin(o.l);
  }
  public void visit(Lmunop o) {
    String s = o.o.toString();
    if(o.m instanceof Maddi){
      Maddi aux_m = (Maddi)o.m;
      if(aux_m.n > 0)      
        asm.addq("$" + aux_m.n, s);
      else if(aux_m.n < 0)
        asm.subq("$" + -aux_m.n, s);
    }
    else if(o.m instanceof Msetei){
      Msetei aux_m = (Msetei)o.m;
      asm.cmpq(aux_m.n,s);
      asm.sete(s);
    }
    else{
      Msetnei aux_m = (Msetnei)o.m;
      asm.cmpq(aux_m.n,s);
      asm.setne(s); 
    }
    lin(o.l);
  }
  public void visit(Lmbinop o) {
    String s1 = o.o1.toString();
    String s2 = o.o2.toString();
    if(o.m == Mbinop.Mmov){
      asm.movq(s1,s2);
    }
    else if(o.m == Mbinop.Madd){
      asm.addq(s1,s2);
    }
    else if(o.m == Mbinop.Msub){
      asm.subq(s1,s2);
    }
    else if(o.m == Mbinop.Mmul){
      asm.imulq(s1,s2);
    }
    else if(o.m == Mbinop.Mdiv){
      asm.idivq(s1);
    }
    else if(o.m == Mbinop.Msete){
      asm.cmpq(s1,s2);
      asm.sete(s2);
    }
    else if(o.m == Mbinop.Msetne){
      asm.cmpq(s1,s2);
      asm.setne(s2);
    }
    else if(o.m == Mbinop.Msetl){
      asm.cmpq(s1,s2);
      asm.setl(s2);
    }
    else if(o.m == Mbinop.Msetle){
      asm.cmpq(s1,s2);
      asm.setle(s2);
    }
    else if(o.m == Mbinop.Msetg){
      asm.cmpq(s1,s2);
      asm.setg(s2);
    }
    else{
      asm.cmpq(s1,s2);
      asm.setge(s2);
    }
    lin(o.l);
  }
  public void visit(Lcall o) {
    asm.call(o.s);
    lin(o.l);
  }
  public void visit(LTLfun o) {}
  public void visit(LTLfile o) {}

  void jcc(Mbbranch m, String s1, String s2, Label l, boolean inverse){
    asm.cmpq(s1,s2);
    if(inverse){
      if(m == Mbbranch.Mjl)
        asm.jge(l.name);
      else
        asm.jg(l.name);
    }
    else{
      if(m == Mbbranch.Mjl)
        asm.jl(l.name);
      else
        asm.jle(l.name);  
    }
  }

  void jcc(Mubranch m, String s, Label l, boolean inverse){
    int value;
    if(m instanceof Mjz || m instanceof Mjnz)
      value = 0;
    else if(m instanceof Mjlei)
      value = ((Mjlei)m).n;
    else if(m instanceof Mjgei)
      value = ((Mjgei)m).n;
    else if(m instanceof Mjli)
      value = ((Mjli)m).n;
    else
      value = ((Mjgi)m).n;

    asm.cmpq(value,s);
    if(inverse){
      if(m instanceof Mjz)
        asm.jne(l.name);
      else if(m instanceof Mjnz)
        asm.je(l.name);
      else if(m instanceof Mjlei)
        asm.jg(l.name);
      else if(m instanceof Mjgei)
        asm.jl(l.name);
      else if(m instanceof Mjli)
        asm.jge(l.name);
      else
        asm.jle(l.name);
    }
    else{
      if(m instanceof Mjz)
        asm.je(l.name);
      else if(m instanceof Mjnz)
        asm.jne(l.name);
      else if(m instanceof Mjlei)
        asm.jle(l.name);
      else if(m instanceof Mjgei)
        asm.jge(l.name);
      else if(m instanceof Mjli)
        asm.jl(l.name);
      else
        asm.jg(l.name);
    }
  }

}