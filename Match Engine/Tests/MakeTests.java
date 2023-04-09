public class MakeTests{
	public static void main(String[] args){
		int total = 1000000;
		for (int i = 1; i <= total; i++) {
            System.out.println("limit buy "+i+" "+i);
        }
        //for (int i = 1; i <= total/2; i++) {
        //    System.out.println("limit sell "+i+" "+i);
        //}
        //for (int i = total/2; i <= total; i++) {
        //    System.out.println("market sell "+i);
        //}
        System.out.println("limit sell "+1+" "+100);
	}
}