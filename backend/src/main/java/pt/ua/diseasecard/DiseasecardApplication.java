package pt.ua.diseasecard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pt.ua.diseasecard.configuration.DiseasecardProperties;

@SpringBootApplication
@EnableConfigurationProperties(DiseasecardProperties.class)
public class DiseasecardApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiseasecardApplication.class, args);
	}

}
