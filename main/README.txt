既知の問題
1. resource/jdk*.jar内に含まれるクラス java.nio.ByteBuffer には
 - byte[] array();
 - java.lang.Object array();
 が定義されている．しかしこのような使用はJava言語の文法違反であり，masuで想定している範囲を超えている．
 そのため，resource/jdk*.jarを -b で指定して，かつ java.lang.Object を解析対象とした場合，
 正しく解析できず，アサーションで止まる場合がある．
 
 2. 1.と関連するが，resource/jdk*.jar内に含まれるクラスjava.math.RoundingModeには
  - java.math.RoundingMode valueOf(java.lang.String)
  - java.math.RoundingMode valueOf(int)
 が定義されている．しかし，jdkのソースには前者の定義がないため，正しく解析できない場合がある．