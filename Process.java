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

  // �?プロセスの�?要時�?
  public double need_time;
  // �?プロセスにおける生産予定数
  public double process_num;
  // �?プロセスにおける稼働可能人数
  public double n_human;
  // �?プロセスの余裕度
  public double margin;

  // TODO:indexで管�?
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
    case -1:
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
      m = "1�?";
      break;
    case 1:
      m = "2�?";
      break;
    case 2:
      m = "3�?";
      break;
    case 3:
      m = "4�?";
      break;
    case 4:
      m = "5�?";
      break;
    case 5:
      m = "6�?";
      break;
    default:
      m = "MonthError";
      break;
    }

    return (p + ", " + l + ", " + m);
  }

  public static void main(String[] args) {
    // TODO: �?定義場�?は?�?
    int proc_len = Constants.ProcMin.length;
    int lv_len = Constants.ProcMin[0].length;
    int month_len = Constants.ProcNum[0].length;

    // レベル1以上�?�現在の人数
    int n_human_now[][][] = new int[proc_len][lv_len][month_len];
    for (int p = 0; p < proc_len; p++) {
      for (int l = 0; l < lv_len; l++) {
        for (int m = 0; m < month_len; m++){
        n_human_now[p][l][m] = Constants.n_human_before[l];
        }
      }
    }

    Process process[][][] = new Process[proc_len][lv_len][month_len];
    // FIXME:ソート用の1次�?配�??
    Process process_1d[] = new Process[proc_len * lv_len * month_len];

    boolean complete[][] = new boolean[proc_len][lv_len];

    for (int i = 0; i < proc_len; i++) {
      for (int j = 0; j < lv_len; j++) {
        complete[i][j] = false;
      }
    }

    // ここから計�?
    for (int loop = 0; loop < proc_len * lv_len * month_len; loop++) {
      boolean all_complete = true;
      System.out.println(loop);
      int idx = 0;
      // �?レベル以上�?�生産可能人数合�?
      // int sum_human[] = new int[proc_len];
      int sum_human[] = new int[month_len];
      for (int m = 0; m < month_len; m++) {
        for (int p = 0; p < proc_len; p++) {
          sum_human[m] += n_human_now[p][0][m];
        }
        System.out.println("sum_human : " + sum_human[m]);
      }

      // for (int i = 0; i < 2; i++){
      // System.out.println("sum_human : " + i + " : " + sum_human);
      // }

      // 全体余裕度の計�?
      // FIXME:メソ�?ド�??り�?�?
      Map<Integer, Double> monthly_margin = new HashMap<Integer, Double>();

      double sum_need_human[] = new double[month_len];
      for (int p = 0; p < proc_len; p++) {
        for (int l = 0; l < lv_len; l++) {
          for (int m = 0; m < month_len; m++) {
            process[p][l][m] = new Process(Constants.ProcMin[p][l], Constants.ProcNum[Constants.ProcPart[p][l]][m],
                n_human_now[p][l][m], p, l, m);
            process_1d[idx] = new Process(Constants.ProcMin[p][l], Constants.ProcNum[Constants.ProcPart[p][l]][m],
                n_human_now[p][l][m], p, l, m);
            sum_need_human[m] += process[p][l][m].need_human();
            idx++;
          }
        }
      }
      for (int m = 0; m < month_len; m++){
        monthly_margin.put(m, sum_need_human[m]/sum_human[m]);
      }
      List<Entry<Integer, Double>> list_entries = new ArrayList<Entry<Integer, Double>>(monthly_margin.entrySet());
      Collections.sort(list_entries, new Comparator<Entry<Integer, Double>>() {
        public int compare(Entry<Integer, Double> obj1, Entry<Integer, Double> obj2) {
          // �?�?
          return obj1.getValue().compareTo(obj2.getValue());
        }
      });
      // 全体余裕度計算ここまで
      for (Entry<Integer, Double> entry : list_entries) {
        System.out.println(entry.getKey() + " : " + entry.getValue());
      }

      // sort
      Arrays.sort(process_1d, Comparator.comparing(Process::getMargin).reversed());

      int target_process = 0;
      int target_lv = 0;
      int target_month = 0;
      for (int i = 0; i < (proc_len * lv_len * month_len); i++) {
        // System.out.println(i);
        target_process = process_1d[i].process;
        target_lv = process_1d[i].lv;
        target_month = process_1d[i].month;
        if (target_month == 0) {
          continue;
        }
        if (complete[target_process][target_lv] == false) {
          // System.out.println("target_month : " + target_month);
          break;
        }
      }
      int ojt_lv = target_lv - 1;
      int ojt_month = 0;
      // System.out.println(process[target_process][target_lv][target_month].Print());
      for (Entry<Integer, Double> entry : list_entries) {
        ojt_month = entry.getKey();
        if (ojt_month >= target_month) {
          continue;
        }
        if (ojt_lv < 0) {
          break;
        }
        double tmp_human = process[target_process][ojt_lv][ojt_month].need_human();
        if (tmp_human / (n_human_now[target_process][ojt_lv][ojt_month] - 0.5) >= 1) {
          continue;
        }
        break;
      }
      for (int m = ojt_month+1; m < month_len; m++){
        n_human_now[target_process][target_lv][m]++;
      }
      if (n_human_now[target_process][target_lv][target_month] >= Constants.n_human_after[target_lv]) {
        complete[target_process][target_lv] = true;
      }
      // 標準�?��?
      System.out.println(process[target_process][target_lv][ojt_month].Print());
      for (int i = 0; i < proc_len; i++) {
        for (int j = 0; j < lv_len; j++) {
          if (complete[i][j] == false) {
            all_complete = false;
          }
        }
      }
      if (all_complete)
        break;
    }

  }
}
