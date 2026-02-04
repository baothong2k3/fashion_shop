package fit.fashion_shop;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import java.util.TimeZone;

@SpringBootApplication
@EnableJpaAuditing
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
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

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
