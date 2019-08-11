package io.github.adyel.serverbrowser;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

public class ServerBrowser extends Application {

    private ConfigurableApplicationContext context;

    /**
     * Initialize Spring boot before JavaFx application starts.
     * @throws Exception Could throw exception.
     */

    @Override
    public void init() throws Exception {

        ApplicationContextInitializer<GenericApplicationContext> initializer = applicationContext -> {
            applicationContext.registerBean(Application.class, () -> ServerBrowser.this);
            applicationContext.registerBean(Parameters.class, this::getParameters);
            applicationContext.registerBean(HostServices.class, this::getHostServices);
        };

        this.context  = new SpringApplicationBuilder()
                .sources(ServerBrowserApplication.class)
                .initializers(initializer)
                .run(getParameters().getRaw().toArray(new String[0]));
    }

    /**
     *
     * @param primaryStage The primary stage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.context.publishEvent(new StageReady(primaryStage));
    }


    @Override
    public void stop() throws Exception {
        this.context.close();
        Platform.exit();
    }
}


class StageReady extends ApplicationEvent {

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    StageReady(Stage source) {
        super(source);
    }

    Stage getStage(){
        return (Stage) getSource();
    }
}