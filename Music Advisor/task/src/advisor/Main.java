package advisor;

import java.util.Scanner;

public class Main {
    static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {

        if(args.length > 1) {
            for(int i = 0; i + 1 < args.length; i = i + 2) {
                switch (args[i]) {
                    case "-access":
                        MyAuthorization.server_path = args[i + 1];
                        break;
                    case "-resource":
                        MyAuthorization.api_server_path = args[i + 1];
                        break;
                    case "-page":
                        MyAuthorization.quantityOfElem = Integer.parseInt(args[i + 1]);
                        break;
                }
            }
        }

        run();
    }

    static void run() throws Exception {

        MyAuthorization authorization = new MyAuthorization();
        boolean access = false;

        System.out.println("Options:\n" +
                "auth\n" +
                "new\n" +
                "featured\n" +
                "categories\n" +
                "playlists \"name of category\"\n");

        String option = scanner.nextLine().trim();
        while (!option.equals("exit")){
            int temp = 0;
            switch (option.split(" ")[0]){
                case "new":
                    if(access) {
                        authorization.newAlbums();
                        authorization.printFirst(3);
                        do {
                            temp = -1;
                            option = scanner.nextLine().trim();
                            if (option.equals("next")) {
                                authorization.next(3, 1);
                            } else if(option.equals("prev")) {
                                authorization.next(3, -1);
                            }
                        } while (option.equals("next") || option.equals("prev"));
                    } else
                        errorMassage();
                    break;
                case "featured":
                    if(access) {
                        authorization.featured();
                        authorization.printFirst(2);
                        do {
                            temp = -1;
                            option = scanner.nextLine().trim();
                            if (option.equals("next")) {
                                authorization.next(2, 1);
                            } else if(option.equals("prev")) {
                                authorization.next(2, -1);
                            }
                        } while (option.equals("next") || option.equals("prev"));
                    }
                    else
                        errorMassage();
                    break;
                case "categories":
                    if(access) {
                        authorization.getCategories();
                        authorization.printFirst(1);
                        do {
                            temp = -1;
                            option = scanner.nextLine().trim();
                            if (option.equals("next")) {
                                authorization.next(1, 1);
                            } else if(option.equals("prev")) {
                                authorization.next(1, -1);
                            }
                        } while (option.equals("next") || option.equals("prev"));
                    }
                    else
                        errorMassage();
                    break;
                case "playlists":
                    if(access) {
                        authorization.getCategoryByName(option.substring(10).trim());
                        authorization.printFirst(2);
                        do {
                            temp = -1;
                            option = scanner.nextLine().trim();
                            if (option.equals("next")) {
                                authorization.next(2, 1);
                            } else if(option.equals("prev")) {
                                authorization.next(2, -1);
                            }
                        } while (option.equals("next") || option.equals("prev"));
                    }
                    else
                        errorMassage();
                    break;
                case "exit":
                    System.out.println("---GOODBYE!---");
                    break;
                case "auth":
                    authorization.server();
                    authorization.postRequest();
                    access = true;
                    break;
            }
            if(temp == 0) {
                option = scanner.nextLine().trim();
            }
        }
    }

    static void errorMassage(){
        System.out.println("Please, provide access for application.");
    }
}
