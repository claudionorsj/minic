package mini_c;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;

class Liveness{
  final private ERTLgraph ertlgraph;
  final HashMap<Label, LiveInfo> info = new HashMap<Label,LiveInfo>();

  Liveness(ERTLgraph ertlgraph) {
    this.ertlgraph = ertlgraph;
    for(HashMap.Entry<Label,ERTL> entry_l_ertl : ertlgraph.graph.entrySet()){
      Label l = entry_l_ertl.getKey();
      ERTL ertl = entry_l_ertl.getValue();
      info.put(l,new LiveInfo(ertl));
    }
    for(HashMap.Entry<Label,LiveInfo> entry_l_linfo : info.entrySet()){
      Label l = entry_l_linfo.getKey();
      Label[] succs = entry_l_linfo.getValue().succ;
      for(Label succ : succs)
        info.get(succ).pred.add(l);
    }
    runKildall();
  }

  private void runKildall(){
    HashSet<Label> ws = new HashSet<Label>(info.keySet());
    Iterator<Label> it;
    Label l;
    HashSet<Register> old_in;
    HashSet<Register> in;
    HashSet<Register> out;
    HashSet<Register> aux_set;
    Label[] succs;
    while(!ws.isEmpty()){
      it = ws.iterator();
      l = it.next();
      it.remove();
      old_in = new HashSet<Register>(info.get(l).ins);
      out = info.get(l).outs;
      in = info.get(l).ins;
      succs = info.get(l).succ;
      out.clear();
      for(Label succ : succs)
        out.addAll(info.get(succ).ins);
      in.clear();
      in.addAll(info.get(l).uses);
      aux_set = new HashSet<Register>(out);
      for(Register r : info.get(l).defs)
        aux_set.remove(r);
      in.addAll(aux_set);
      if(!in.equals(old_in))
        ws.addAll(info.get(l).pred);
    }
  }

  private void print(Set<Label> visited, Label l) {
    if (visited.contains(l)) return;
    visited.add(l);
    LiveInfo li = this.info.get(l);
    System.out.println("  " + String.format("%3s", l) + ": " + li);
    for (Label s: li.succ) print(visited, s);
  }

  void print(Label entry) {
    print(new HashSet<Label>(), entry);
  }
}

class LiveInfo {
  ERTL instr;
  Label[] succ;
  HashSet<Label> pred;
  HashSet<Register> defs;
  HashSet<Register> uses;
  HashSet<Register> ins;
  HashSet<Register> outs;

  LiveInfo(ERTL instr){
    this.instr = instr;
    this.succ = instr.succ();
    this.defs = (HashSet<Register>)instr.def();
    this.uses = (HashSet<Register>)instr.use();
    this.pred = new HashSet<Label>();
    this.ins = new HashSet<Register>();
    this.outs = new HashSet<Register>();
  }

  public String toString(){
    String ret_s = instr + " d="+defs+" u="+uses+" i="+ins+" o="+outs;
    return ret_s;
  }
}