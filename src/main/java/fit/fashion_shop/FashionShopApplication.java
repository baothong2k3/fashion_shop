package fit.fashion_shop;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FashionShopApplication {
    public static void main(String[] args) {
        // Nạp file .env
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        // Đẩy từng biến trong .env vào System Properties để Spring có thể đọc được qua ${VARIABLE_NAME}
        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
        });

        SpringApplication.run(FashionShopApplication.class, args);
    }
}
