package titan.lightbatis.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication

public class TitanDalApplication {

//    @Autowired
//    private DataSource dataSource = null;

    public TitanDalApplication() {
    }

    public static void main(String[] args) {
        System.out.println("===============>>>>>>>>>>>>>>>>>>==============");
       SpringApplication.run(TitanDalApplication.class,args);
        System.out.println("" +
                "__________                    .__                \n" +
                "\\______   \\__ __  ____   ____ |__| ____    ____  \n" +
                " |       _/  |  \\/    \\ /    \\|  |/    \\  / ___\\ \n" +
                " |    |   \\  |  /   |  \\   |  \\  |   |  \\/ /_/  >\n" +
                " |____|_  /____/|___|  /___|  /__|___|  /\\___  / \n" +
                "        \\/   启动成功  \\/     \\/ Lightbatis DAL 控制台 \\//_____/  ");
    }




}
