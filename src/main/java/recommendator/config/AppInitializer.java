package recommendator.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    /**
     * We accept all incoming requests starting at /
     *
     * @return All the mappings we accept
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    /**
     * The classes we use for configuring our ApplicationContext
     *
     * @return An array containing configuration classes for our ApplicationContext
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{AppConfig.class};
    }

    /**
     * We have no special DispatcherServlet logic, so we do all the configuration in {@link AppConfig}
     * It is useful if you have multiple DispatcherServlets and you want to specifically manage the different
     * WebApplicationContexts
     * https://stackoverflow.com/questions/35258758/getservletconfigclasses-vs-getrootconfigclasses-when-extending-abstractannot
     *
     * @return null, as it's not necessary for us.
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return null;
    }
}
