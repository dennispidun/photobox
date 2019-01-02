package me.dpidun.photobox.config;

import me.dpidun.photobox.photo.ImageLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SinglePageAppConfig implements WebMvcConfigurer {

    private static final String FORWARD_INDEX = "forward:/index.html";

    @Autowired
    private ImageLocationService imageLocationService;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName(FORWARD_INDEX);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {


        if (!registry.hasMappingForPattern("/assets/photos/**")) {
            registry.addResourceHandler("/assets/photos/**")
                .addResourceLocations("file:///" + imageLocationService.getImageLocation() + "/");
        }
    }
}