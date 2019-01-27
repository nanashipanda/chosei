import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Process {

  public double work_day = 20;
  public double work_time = 7;

  public double month_work_time = this.work_day * this.work_time * 60;

  public double need_time;
  public double process_num;
  public double human_num;
  public int month;
  public int process;
  public int lv;
  public double yoyudo;

  public Process(double need_time, double process_num, double human_num, int process, int lv, int month){
    this.need_time = need_time;
    this.process_num = process_num;
    this.human_num = human_num;
    this.month = month;
    this.process = process;
    this.lv = lv;
    this.yoyudo();
  }

  private double can_make_amount() {
    return month_work_time / this.need_time;
  }

  private double need_human() {
    return this.process_num / this.can_make_amount();
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

  private double yoyudo() {
    this.yoyudo = this.need_human() / this.human_num;
    // return this.need_human() / this.human_num;
    return this.yoyudo;
  }

  private int compareTo(Process process){
    if (this.yoyudo() < process.yoyudo()) {
      return -1;
    } else if (this.yoyudo() > process.yoyudo()) {
      return 1;
    } else {
      return 0;
    }
  }

  public static void main(String[] args) {
    int[][] proc = {{15, 15, 10}, {15, 14, 12}, {14, 9, 15}, {13, 8, 10}, {10, 12, 11}};

    int[] lv = {15, 12, 7, 3};
    int[] lv_ojt = {15, 15, 9, 4};

    int[][][] proc_num = {{
                      {1600, 1000, 1200, 1400, 1800, 1000},
                      {1000, 1600, 1200, 1400, 1800, 1800},
                      {2100, 1500, 2200, 1800, 2200, 2000}
                       },
                      {
                       {2100, 1500, 2200, 1800, 2200, 2000},
                       {2000, 2500, 1500, 2200, 2000, 2400},
                       {1000, 1600, 1200, 1400, 1800, 1800}
                      },
                      {
                       {1000, 1600, 1200, 1400, 1800, 1800},
                       {1600, 1000, 1200, 1400, 1800, 1000},
                       {2000, 2500, 1500, 2200, 2000, 2400}
                      },
                      {
                        {2000, 2500, 1500, 2200, 2000, 2400},
                        {2100, 1500, 2200, 1800, 2200, 2000},
                        {1600, 1000, 1200, 1400, 1800, 1000}
                      },
                      {
                        {1600, 1000, 1200, 1400, 1800, 1000},
                        {1000, 1600, 1200, 1400, 1800, 1800},
                        {2100, 1500, 2200, 1800, 2200, 2000}
                      }};
    Process p[] = new Process[90];
    int idx = 0;
    for(int i = 0; i < 5; i++){ // process
      for(int j = 0; j < 3; j++){ //lv
        for(int k = 0; k < 6; k++){ //month
          p[idx] = new Process(proc[i][j], proc_num[i][j][k], lv[j+1], i, j+1, k);
          idx++;
        }
      }
    }

    // Arrays.sort(p, (a,b) -> a.yoyudo - b.yoyudo);

    for(int i = 0; i < 90; i++){
      System.out.println(p[i].getProcess() +","+ p[i].getLv() +","+ p[i].getMonth() + " : " + p[i].yoyudo());
    }
  }
}
