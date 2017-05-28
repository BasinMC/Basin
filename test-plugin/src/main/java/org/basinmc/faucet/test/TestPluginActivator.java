package org.basinmc.faucet.test;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import javax.annotation.Nonnull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.basinmc.faucet.Server;
import org.ops4j.peaberry.Peaberry;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public class TestPluginActivator implements BundleActivator, Module {

  private Injector injector;

  /**
   * {@inheritDoc}
   */
  @Override
  public void configure(@Nonnull Binder binder) {
    binder.bind(Server.class).toProvider(Peaberry.service(Server.class).single());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void start(@Nonnull BundleContext context) throws Exception {
    this.injector = Guice.createInjector(Peaberry.osgiModule(context), this);

    Logger logger = LogManager.getFormatterLogger(TestPluginActivator.class);
    logger.info("Hello World!");
    logger.info("Server Version: %s", this.injector.getInstance(Server.class).getVersion());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void stop(@Nonnull BundleContext context) throws Exception {
  }
}
