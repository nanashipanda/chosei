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

  private String getProcess() {
    switch (this.process) {
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
    switch (this.lv) {
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
    switch (this.month) {
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
    // レベル1以上の現在の人数
    int h_len = Constants.n_human_before.length;
    int n_human_now[] = new int[h_len];
    for (int i = 0; i < h_len; i++) {
      n_human_now[i] = Constants.n_human_before[i];
    }

    // TODO: 各定義場所は？
    int proc_len = Constants.ProcMin.length;
    int lv_len = Constants.ProcMin[0].length;
    int month_len = Constants.ProcNum[0].length;

    Process process[][][] = new Process[proc_len][lv_len][month_len];
    // FIXME:ソート用の1次元配列
    Process process_1d[] = new Process[proc_len * lv_len * month_len];

    //////
    double m_margin[] = new double[month_len];
    Map<Integer, Double> monthly_margin = new HashMap<Integer, Double>();

    ///////

    int idx = 0;
    // 各レベル以上の生産可能人数合計
    int sum_human = 0;
    for (int i = 0; i < n_human_now.length; i++) {
      sum_human += n_human_now[i];
    }

    for (int p = 0; p < proc_len; p++) {
      for (int l = 0; l < lv_len; l++) {
        for (int m = 0; m < month_len; m++) {
          process[p][l][m] = new Process(Constants.ProcMin[p][l], Constants.ProcNum[Constants.ProcPart[p][l]][m],
              n_human_now[l], p, l, m);
          process_1d[idx] = new Process(Constants.ProcMin[p][l], Constants.ProcNum[Constants.ProcPart[p][l]][m],
              n_human_now[l], p, l, m);
          // System.out.println(process[p][l][m].Print() + " : " +
          // process[p][l][m].getMargin());
          // System.out.println(process[p][l][m].Print() + " : " +
          // process[p][l][m].need_human());
          idx++;
          if (p == 0 && l == 0) {
            monthly_margin.put(m, (process[p][l][m].need_human()) / (sum_human));
          } else {
            monthly_margin.put(m, (monthly_margin.get(m) + (process[p][l][m].need_human()) / (sum_human)));
          }
          m_margin[m] += (process[p][l][m].need_human()) / (sum_human);
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
    System.out.println(m_margin[0]);
    // sort
    Arrays.sort(process_1d, Comparator.comparing(Process::getMargin).reversed());
    for (Entry<Integer, Double> entry : list_entries) {
      System.out.println(entry.getKey() + " : " + entry.getValue());
    }

    int loop = 0;
    while (loop < 5) {
      int p = process_1d[loop].process;
      int l = process_1d[loop].lv;
      int m = process_1d[loop].month;
      double margin = process_1d[loop].getMargin();

      if (process_1d.length <= 0)
        break;
      loop++;
    }
  }
}
