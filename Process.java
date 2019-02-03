import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.*;

import java.util.Map;
import java.util.Map.Entry;
import java.util.List;
import java.util.Comparator;
// import Constants;

public class Process {

  // 各プロセスの必要時間
  public double need_time;
  // 各プロセスにおける生産予定数
  public double process_num;
  // 各プロセスにおける稼働可能人数
  public double n_human;
  // 各プロセスの余裕度
  public double margin;

  // TODO:indexで管理
  public int month;
  public int process;
  public int lv;
  //

  public Process(double need_time, double process_num, double n_human, int process, int lv, int month) {
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

  private String Print() {
    String p = "";
    switch (this.process) {
    case 0:
      p = "A";
      break;
    case 1:
      p = "B";
      break;
    case 2:
      p = "C";
      break;
    case 3:
      p = "D";
      break;
    case 4:
      p = "E";
      break;
    default:
      p = "ProcessError";
      break;
    }

    String l = "";
    switch (this.lv) {
    case 9:
      l = "Lv0";
      break;
    case 0:
      l = "Lv1";
      break;
    case 1:
      l = "Lv2";
      break;
    case 2:
      l = "Lv3";
      break;
    default:
      l = "LvError";
      break;
    }

    String m = "";
    switch (this.month) {
    case 0:
      m = "1月";
      break;
    case 1:
      m = "2月";
      break;
    case 2:
      m = "3月";
      break;
    case 3:
      m = "4月";
      break;
    case 4:
      m = "5月";
      break;
    case 5:
      m = "6月";
      break;
    default:
      m = "MonthError";
      break;
    }

    return (p + ", " + l + ", " + m);
  }


  public static void main(String[] args) {
    // TODO: 各定義場所は？
    int proc_len = Constants.ProcMin.length;
    int lv_len = Constants.ProcMin[0].length;
    int month_len = Constants.ProcNum[0].length;

    // レベル1以上の現在の人数
    int h_len = Constants.n_human_before.length;
    int n_human_now[][] = new int[proc_len][h_len];
    for (int i = 0; i < proc_len; i++){
      for (int j = 0; j < h_len; j++){
        n_human_now[i][j] = Constants.n_human_before[j];
      }
   }

    Process process[][][] = new Process[proc_len][lv_len][month_len];
    // FIXME:ソート用の1次元配列
    Process process_1d[] = new Process[proc_len * lv_len * month_len];



    int idx = 0;
    // 各レベル以上の生産可能人数合計
    int sum_human[] = new int[proc_len];
    for (int i = 0; i < proc_len; i++) {
      for (int j = 0; j < h_len; j++){
        sum_human[i] += n_human_now[i][j];
      }
    }
    Map<Integer, Double> monthly_margin = new HashMap<Integer, Double>();

    for (int p = 0; p < proc_len; p++) {
      for (int l = 0; l < lv_len; l++) {
        for (int m = 0; m < month_len; m++) {
          process[p][l][m] = new Process(Constants.ProcMin[p][l], Constants.ProcNum[Constants.ProcPart[p][l]][m],
              n_human_now[p][l], p, l, m);
          process_1d[idx] = new Process(Constants.ProcMin[p][l], Constants.ProcNum[Constants.ProcPart[p][l]][m],
              n_human_now[p][l], p, l, m);
          idx++;
          if (p == 0 && l == 0) {
            monthly_margin.put(m, (process[p][l][m].need_human()) / (sum_human[p]));
          } else {
            monthly_margin.put(m, (monthly_margin.get(m) + (process[p][l][m].need_human()) / (sum_human[p])));
          }
        }
      }
    }
    // 2.Map.Entryのリストを作成する
    List<Entry<Integer, Double>> list_entries = new ArrayList<Entry<Integer, Double>>(monthly_margin.entrySet());

    // 3.比較関数Comparatorを使用してMap.Entryの値を比較する(昇順)
    Collections.sort(list_entries, new Comparator<Entry<Integer, Double>>() {
      public int compare(Entry<Integer, Double> obj1, Entry<Integer, Double> obj2) {
        // 4. 昇順
        return obj1.getValue().compareTo(obj2.getValue());
      }
    });
    // sort
    Arrays.sort(process_1d, Comparator.comparing(Process::getMargin).reversed());

    int target_month = process_1d[0].month;
    int target_process = process_1d[0].process;
    int target_lv = process_1d[0].lv;
    for (Entry<Integer, Double> entry : list_entries) {
      if (entry.getKey() >= target_month){
        continue;
      }
      double tmp_human = process[target_process][target_lv-1][entry.getKey()].need_human();
      if (tmp_human / (n_human_now[target_process][target_lv-1] - 0.5) >= 1){
        continue;
      }
      // System.out.println(entry.getKey() + " : " + entry.getValue());

      n_human_now[target_process][target_lv]++;
      break;
    }

    for(int i = 0; i < 5; i++){
      for (int j = 0; j < 3; j++){
        System.out.println(n_human_now[i][j]);
      }
    }
  }
}
