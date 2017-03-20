package mini_c;

import java.util.LinkedList;

abstract class Inst extends Base{
  abstract Label generate_rtl(Label next_l, Register return_r, Label return_l);
}
