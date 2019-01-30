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
  public double n_human;
  //各プロセスの余裕度
  public double margin;

  //TODO:indexで管理
  public int month;
  public int process;
  public int lv;
  //

  public Process(double need_time, double process_num, double n_human, int process, int lv, int month){
    this.need_time = need_time;
    this.process_num = process_num;
    this.n_human = n_human;
    this.process = process;
    this.lv = lv;
    this.month = month;
  }

  private double producible() {
    return Constants.work_time / this.need_time;
  }

  private double need_human() {
    return this.process_num / this.producible();
  }

  public double getMargin() {
    this.margin = this.need_human() / this.n_human;
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
      case 9:
        return "Lv0";
      case 0:
        return "Lv1";
      case 1:
        return "Lv2";
      case 2:
        return "Lv3";
      default:
        return "LvError";
    }
  }

  private String getMonth() {
    switch(this.month) {
    case 0:
      return "1月";
    case 1:
      return "2月";
    case 2:
      return "3月";
    case 3:
      return "4月";
    case 4:
      return "5月";
    case 5:
      return "6月";
    default:
      return "MonthError";
  }
}


  public static void main(String[] args) {
    //レベル1以上の現在の人数
    int h_len = Constants.n_human_before.length;
    int n_human_now[] = new int[h_len];
    for( int i = 0; i < h_len; i++){
      n_human_now[i] = Constants.n_human_before[i];
    }

    //TODO: 各定義場所は？
    int proc_len = Constants.ProcMin.length;
    int lv_len = Constants.ProcMin[0].length;
    int month_len = Constants.ProcNum[0].length;

    Process process[][][] = new Process[proc_len][lv_len][month_len];
    //FIXME:ソート用の1次元配列
    Process process_1d[] = new Process[proc_len * lv_len * month_len];

    double monthly_margin[] = new double[month_len];

    int idx = 0;
    //各レベル以上の生産可能人数合計
    int sum_human = 0;
    for (int i = 0; i < n_human_now.length; i++){
      sum_human += n_human_now[i];
    }

    for (int p = 0; p < proc_len; p++){
      for (int l = 0; l < lv_len; l++){
        for (int m = 0; m < month_len; m++){
          process[p][l][m] = new Process(Constants.ProcMin[p][l], Constants.ProcNum[Constants.ProcPart[p][l]][m], n_human_now[l], p, l, m);
          process_1d[idx] = new Process(Constants.ProcMin[p][l], Constants.ProcNum[Constants.ProcPart[p][l]][m], n_human_now[l], p, l, m);
          // monthly_margin[m] += (process[p][l][m].need_human()) / (sum_human);
          System.out.println(process[p][l][m].getProcess() + "," + process[p][l][m].getLv() + "," + process[p][l][m].getMonth() + " : " + process[p][l][m].getMargin());
          idx++;
        }
      }
    }
    //sort
    Arrays.sort(process_1d, Comparator.comparing(Process::getMargin).reversed());


  }
}
