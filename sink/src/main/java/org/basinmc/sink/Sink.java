package org.basinmc.sink;

import edu.umd.cs.findbugs.annotations.NonNull;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.basinmc.faucet.event.EventBus;
import org.basinmc.faucet.event.system.StartupEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Provides utility methods which permit the initialization of the Basin application and plugin
 * context during the server startup.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public class Sink implements AutoCloseable {

  private static final Logger logger = LogManager.getFormatterLogger(Sink.class);

  private final AnnotationConfigApplicationContext context;

  public Sink(@NonNull MinecraftServer server) {
    this.context = new AnnotationConfigApplicationContext();
    this.context.getBeanFactory().registerSingleton("minecraftServer", server);

    this.context.scan(this.getClass().getPackageName());
  }

  public void onStart() {
    logger.info("Basin Sink v%s entered startup", SinkVersion.IMPLEMENTATION_VERSION);

    logger.debug("Performing Spring Context initialization");
    this.context.refresh();
    this.context.start();

    var eventBus = this.context.getBean(EventBus.class);

    eventBus.post(new StartupEvent.Pre());
    {
      // TODO: Initialize plugin system
      // TODO: Publish startup event
    }
    eventBus.post(new StartupEvent.Post());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void close() {
    logger.info("Sink is shutting down");

    // TODO: Publish close event
    this.context.close();
  }
}
