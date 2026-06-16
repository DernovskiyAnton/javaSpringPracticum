package org.example.config;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {


public static final String UPLOAD_TEMP_DIR = "\"/tmp\"";
    public static final long MAX_FILE_SIZE = 10 * 1024 * 1024;
    public static final long MAX_REQUEST_SIZE = 20 * 1024 * 1024;
    public static final int FILE_SIZE_Threshold = 0;

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {AppConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {WebConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        registration.setMultipartConfig(
                new MultipartConfigElement(UPLOAD_TEMP_DIR,
                        MAX_FILE_SIZE, MAX_REQUEST_SIZE,
                        FILE_SIZE_Threshold)
        );
    }
}
