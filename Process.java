import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.*;
import Constants;

public class Process {

  //各プロセスの必要時間
  public double need_time;
  //各プロセスにおける生産予定数
  public double process_num;
  //各プロセスにおける稼働可能人数
  public double human_num;
  //各プロセスの余裕度
  public double margin;

  //TODO:indexで管理
  public int month;
  public int process;
  public int lv;
  //

  public Process(double need_time, double process_num, double human_num, int process, int lv, int month){
    this.need_time = need_time;
    this.process_num = process_num;
    this.human_num = human_num;
    this.month = month;
    this.process = process;
  }

  private double producible() {
    return Constants.work_time / this.need_time;
  }

  private double need_human() {
    return this.process_num / this.producible();
  }

  public double getMargin() {
    this.margin = this.need_human() / this.human_num;
    return this.margin;
  }

  private String getProcess() {
    switch(this.process) {
      case 0:
        return "A";
      case 1:
        return "B";
      case 2:
        return "C";
      case 3:
        return "D";
      case 4:
        return "E";
      default:
        return "ProcError";
    }
  }

  private String getLv() {
    switch(this.lv) {
      case 0:
        return "Lv0";
      case 1:
        return "Lv1";
      case 2:
        return "Lv2";
      case 3:
        return "Lv3";
      default:
        return "LvError";
    }
  }

  private String getMonth() {
    switch(this.month) {
    case 0:
      return "1";
    case 1:
      return "2";
    case 2:
      return "3";
    case 3:
      return "4";
    case 4:
      return "5";
    case 5:
      return "6";
    default:
      return "MonthError";
  }
}


  public static void main(String[] args) {
    int[] human_num_before = {12, 7, 3};
    int[] human_num_after = {15, 9, 4};

    //TODO: 各定義場所は？
    int proc_len = Constants.ProcMin.length;
    int lv_len = Constants.ProcMin[0].length;
    int month_len = Constants.ProcNum[0].length;

    Process process[][][] = new Process[proc_len][lv_len][month_len];
    for (int p = 0; p < proc_len; p++){
      for (int l = 0; l < lv_len; l++){
        for (int m = 0; m < month_len; m++){
          process[p][l][m] = new Process(Constants.ProcMin[p][l], Constants.ProcNum[Constants.ProcPart[p][l]][m], human_num_before[l], p, l, m);
          System.out.println(process[p][l][m].getMargin());
        }
      }
    }
  }
}
