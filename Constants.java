public final class Constants {

    //1ヶ月の稼働日数
    public static final int work_day = 20;
    //1日の稼働時間
    public static final int work_time_hour = 7;
    //1時間 = 60分
    public static final int minutes = 60;
    //1ヶ月の稼働時間(分) = 8400min
    public static final double work_time = work_day * work_time_hour * minutes;

    //各プロセス各レベルの1つの製品を作るのに必要な時間
    public static final int ProcMin[][] = {
        { 15, 15, 10 }, //A lv1, lv2, lv3
        { 12, 15, 14 }, //B lv1, lv2, lv3
        { 15, 14, 9 }, //C lv1, lv2, lv3
        { 8, 10, 13 }, //D lv1, lv2, lv3
        { 10, 12, 11 } //E lv1, lv2, lv3
    };

    //計算の開始月
    //0: 1月, 1: 2月, 2: 3月, ... , n: n+1月
    public static final int start_month = 0;

    //各製品における各月の生産予定個数
    public static final int ProcNum[][] = {
        { 1600, 1000, 1200, 1400, 1800, 1000}, //製品1
        { 2000, 2500, 1500, 2200, 2000, 2400}, //製品2
        { 1000, 1600, 1200, 1400, 1800, 1800}, //製品3
        { 2100, 1500, 2200, 1800, 2200, 2000}, //製品4
    };

    //各プロセスの各レベルがどの製品を担当するか
    //0: 製品1, 1: 製品2, ... , n: 製品(n+1)
    public static final int ProcPart[][] = {
        {0, 2, 3}, //A lv1, lv2, lv3
        {3, 1, 2}, //B lv1, lv2, lv3
        {2, 0, 1}, //C lv1, lv2, lv3
        {1, 3, 0}, //D lv1, lv2, lv3
        {0, 2, 3}, //E lv1, lv2, lv3
    };

    //レベル0の人数
    public static final int lv0 = 15;

    //レベル1以上の初期人数(プロセス毎)
    public static final int n_human_before[][] = {
          { 12, 7, 3 },
          { 12, 7, 3 },
          { 12, 7, 3 },
          { 12, 7, 3 },
          { 12, 7, 3 },
        };
    //レベル1以上のOJT後人数(プロセス毎)
    public static final int n_human_after[][] = {
          { 15, 9, 4 },
          { 15, 9, 4 },
          { 15, 9, 4 },
          { 15, 9, 4 },
          { 15, 9, 4 },
        };

    private Constants (){}
}