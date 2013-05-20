package simple;

public class NestBlock {

    public void block(){
        System.out.println("hoge");
        int a = 0;
        {
            System.out.println("hoge2");
            int b = 100;
            {
                System.out.println("hoge3");
                int c = 200;
                if( b==100 ){
                    int d = 20;
                }
            }
        }
    }

}
