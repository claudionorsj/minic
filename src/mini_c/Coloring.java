package mini_c;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

class Coloring {
  Map<Register, Operand> colors = new HashMap<>();
  int nlocals = 0;

  Coloring(Interference intf){
    HashMap<Register,HashSet<Register>> todo = new HashMap<Register,HashSet<Register>>();
    for(HashMap.Entry<Register,Arcs> entry_r_arcs : intf.graph.entrySet()){
      Register r = entry_r_arcs.getKey();
      if(Register.allocatable.contains(r))
        continue;
      Arcs arcs = entry_r_arcs.getValue();
      HashSet<Register> pot_colors = new HashSet<Register>(Register.allocatable);
      for(Register aux_r : arcs.intfs)
        pot_colors.remove(aux_r);
      todo.put(r,pot_colors);
    }
    Register one_color;
    Register pref_known;
    Operand pref_color;
    Register pref_color_r;
    Register some_color;
    Register paint_r;
    while(!todo.isEmpty()){
      one_color = null;
      pref_known = null;
      pref_color = null;
      pref_color_r = null;
      some_color = null;
      paint_r = null;
      for(HashMap.Entry<Register,HashSet<Register>> entry_r_colors : todo.entrySet()){
        Register r = entry_r_colors.getKey();
        HashSet<Register> pot_colors = entry_r_colors.getValue();
        if(pot_colors.size() == 1){
          Register color = pot_colors.iterator().next();
          if(intf.graph.get(r).prefs.contains(color)){
            paint_r = r;
            break;
          }
          else
            one_color = r;
        }
        else if(one_color == null && pref_known == null){
          for(Register aux_r : intf.graph.get(r).prefs){
            if(pot_colors.contains(aux_r)){
              pref_known = r;
              pref_color_r = aux_r;
              pref_color = new Reg(aux_r);
              break;
            }
            else if(colors.containsKey(aux_r)){
              Operand op = colors.get(aux_r);
              if(op instanceof Reg){
                if(pot_colors.contains(((Reg)op).r)){
                  pref_known = r;
                  pref_color_r = ((Reg)op).r;
                  pref_color = op;
                  break;
                }
              }
              else{
                pref_known = r;
                pref_color = colors.get(aux_r);
              }
            }
          }
          if(pref_known == null && pot_colors.size() > 0){
            some_color = r;
          }
        }
      }

      Register color_r = null;
      if(paint_r != null){
        color_r = todo.get(paint_r).iterator().next();
      }
      else if(one_color != null){
        paint_r = one_color;
        color_r = todo.get(paint_r).iterator().next();
      }
      else if(pref_known != null){
        colors.put(pref_known,pref_color);
        todo.remove(pref_known);
        if(pref_color_r != null)
          for(Register r : intf.graph.get(pref_known).intfs)
            if(todo.containsKey(r))
              todo.get(r).remove(pref_color_r);
        continue;
      }
      else if(some_color != null){
        paint_r = some_color;
        color_r = todo.get(paint_r).iterator().next();
      }
      else{
        Register r = todo.keySet().iterator().next();
        colors.put(r,new Spilled(8*(nlocals++)));
        todo.remove(r);
        continue;
      }
      colors.put(paint_r,new Reg(color_r));
      todo.remove(paint_r);
      for(Register r : intf.graph.get(paint_r).intfs)
        if(todo.containsKey(r))
          todo.get(r).remove(color_r);
    }
  }

  void print() {
    System.out.println("coloring output:");
    for (Register r: colors.keySet()) {
      Operand o = colors.get(r);
      System.out.println("  " + r + " --> " + o);
    }
  }
}