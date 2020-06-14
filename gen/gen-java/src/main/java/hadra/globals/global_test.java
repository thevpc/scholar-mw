package hadra.globals;
import net.vpc.hadralang.stdlib.HDefaults;
public final class global_test{
  private global_test(){}
  static int x;  
  public static void runGlobals(){
    x=0;
    HDefaults.println(x);
  }
}