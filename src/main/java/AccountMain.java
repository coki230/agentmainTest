public class AccountMain {

    public static void main(String[] args) throws InterruptedException {
        for (;;) {
            new Account().operation();
            Thread.sleep(1000);
        }

    }
}