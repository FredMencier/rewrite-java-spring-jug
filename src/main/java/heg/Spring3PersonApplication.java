package heg;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class Spring3PersonApplication {

    private static final Logger LOG = Logger.getLogger(Spring3PersonApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(Spring3PersonApplication.class, args);
        LOG.info("Start Person Springboot application");
    }
}
