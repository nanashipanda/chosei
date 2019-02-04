import java.util.*;
import java.util.Map.Entry;

public class Process {

  // 各プロセスの所要時間
  public double need_time;
  // 各プロセスにおける生産予定数
  public double process_num;
  // 各プロセスにおける稼働可能人数
  public double n_human;
  // 各プロセスの余裕度
  public double margin;

  public int month;
  public int process;
  public int lv;

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
    char a = 'A';
    for(int i=0; i < this.process; i++){
      a++;
    }
    String p = String.valueOf(a);

    String l = "Lv" + (this.lv+1);

    String m = this.month + (Constants.start_month+1) + "月";

    return (p + ", " + l + ", " + m);
  }

  public static void main(String[] args) {
    // TODO: 定義場所の確認
    int proc_len = Constants.ProcMin.length;
    int lv_len = Constants.ProcMin[0].length;
    int month_len = Constants.ProcNum[0].length;

    // レベル1以上の現在の人数(月ごと)
    int n_human_now[][][] = new int[proc_len][lv_len][month_len];
    for (int p = 0; p < proc_len; p++) {
      for (int l = 0; l < lv_len; l++) {
        for (int m = 0; m < month_len; m++){
        n_human_now[p][l][m] = Constants.n_human_before[p][l];
        }
      }
    }

    Process process[][][] = new Process[proc_len][lv_len][month_len];
    // FIXME: ソート用の1次元配列
    Process process_1d[] = new Process[proc_len * lv_len * month_len];

    boolean complete[][] = new boolean[proc_len][lv_len];

    for (int i = 0; i < proc_len; i++) {
      for (int j = 0; j < lv_len; j++) {
        complete[i][j] = false;
      }
    }

    for (int loop = 0; loop < proc_len * lv_len * month_len; loop++) {
      boolean all_complete = true;
      int idx = 0;
      // レベル1以上の月ごとの合計生産可能人数
      int sum_human[] = new int[month_len];
      for (int m = 0; m < month_len; m++) {
        for (int p = 0; p < proc_len; p++) {
          sum_human[m] += n_human_now[p][0][m];
        }
      }

      // FIXME:メソッド切り分け
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
          return obj1.getValue().compareTo(obj2.getValue());
        }
      });
      // 全体余裕度計算ここまで

      // sort of process
      Arrays.sort(process_1d, Comparator.comparing(Process::getMargin).reversed());

      int target_process = 0;
      int target_lv = 0;
      int target_month = 0;
      for (int i = 0; i < (proc_len * lv_len * month_len); i++) {
        target_process = process_1d[i].process;
        target_lv = process_1d[i].lv;
        target_month = process_1d[i].month;
        if (target_month == 0) {
          continue;
        }
        if (complete[target_process][target_lv] == false) {
          break;
        }
      }
      int ojt_lv = target_lv - 1;
      int ojt_month = 0;
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
      if (n_human_now[target_process][target_lv][ojt_month+1] >= Constants.n_human_after[target_process][target_lv]) {
        complete[target_process][target_lv] = true;
      }
      // 標準出力
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
