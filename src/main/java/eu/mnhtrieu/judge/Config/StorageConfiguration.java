package eu.mnhtrieu.judge.Config;


import eu.mnhtrieu.judge.Business.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfiguration {

    @Bean
    CommandLineRunner init(StorageService storageService){
        return (args) -> {
            //storageService.deleteAll();
            storageService.init();
        };
    }
}
