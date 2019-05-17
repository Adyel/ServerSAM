package io.github.adyel.ServerBrowser;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerBrowserApplication {

	public static void main(String[] args) {
		Application.launch(ServerBrowser.class, args);
//		SpringApplication.run(ServerBrowserApplication.class, args);
	}

}
