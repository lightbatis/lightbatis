package titan.lightbatis.web;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


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
                "        \\/   启动成功  \\/     \\/ MyBatis DAL 控制台 \\//_____/  ");
    }




}
