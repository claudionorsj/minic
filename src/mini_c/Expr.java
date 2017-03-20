package mini_c;

abstract class Expr extends Base {
  String type = "";

  abstract Label generate_rtl(Register value_r, Label next_l);

  Label generate_rtl_c(Label true_l, Label false_l){
    Register aux_r = new Register();
    Label aux_l = current_rtlgraph.add(new Rmubranch(new Mjz(), aux_r, false_l, true_l));
    return generate_rtl(aux_r,aux_l);
  }
}
