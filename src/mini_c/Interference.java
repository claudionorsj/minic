package mini_c;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;

class Interference {
  HashMap<Register, Arcs> graph = new HashMap<Register, Arcs>();

  Interference(Liveness lness){
    HashSet<LiveInfo> liveinfos = new HashSet<LiveInfo>(lness.info.values());
    for(LiveInfo liveinfo : liveinfos){
      for(Register def_r : liveinfo.defs){
        // if(Register.callee_saved.contains(def_r))
        //   add_intf(def_r,((ERmbinop)liveinfo.instr).r1);
        if(liveinfo.instr instanceof ERmbinop && ((ERmbinop)liveinfo.instr).m == Mbinop.Mmov){
          Register r = ((ERmbinop)liveinfo.instr).r1;
          if(!r.equals(def_r))
            add_pref(r,def_r);
          for(Register out_r : liveinfo.outs)
            if(!out_r.equals(r) && !out_r.equals(def_r))
              add_intf(def_r,out_r);
        }
        else
          for(Register out_r : liveinfo.outs)
            if(!out_r.equals(def_r))
              add_intf(def_r,out_r);
      }
    }
    for(HashMap.Entry<Register,Arcs> entry_r_arcs : graph.entrySet()){
      Register aux_r = entry_r_arcs.getKey();
      Arcs aux_arcs = entry_r_arcs.getValue();
      for(Register r : aux_arcs.intfs)
        aux_arcs.prefs.remove(r);
    }
  }

  void add_pref(Register r1, Register r2){
    if(!graph.containsKey(r1))
      graph.put(r1, new Arcs());
    if(!graph.containsKey(r2))
      graph.put(r2, new Arcs());
    graph.get(r1).prefs.add(r2);
    graph.get(r2).prefs.add(r1);
  }

  void add_intf(Register r1, Register r2){
    if(!graph.containsKey(r1))
      graph.put(r1, new Arcs());
    if(!graph.containsKey(r2))
      graph.put(r2, new Arcs());
    graph.get(r1).intfs.add(r2);
    graph.get(r2).intfs.add(r1);
  }

  void print() {
    System.out.println("interference:");
    for (Register r: graph.keySet()) {
      Arcs a = graph.get(r);
      System.out.println("  " + r + " pref=" + a.prefs + " intf=" + a.intfs);
    }
  }
}

class Arcs {
  Set<Register> prefs = new HashSet<>();
  Set<Register> intfs = new HashSet<>();
}
