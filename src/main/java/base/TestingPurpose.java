package base;

public class TestingPurpose {

    public static void main(String[] args) {
        String s = "Rock hte ck";
//        System.out.println(());
        int i = s.indexOf("ck");
        int j = s.indexOf("ck", i+2);
        System.out.println(j);
    }
}
