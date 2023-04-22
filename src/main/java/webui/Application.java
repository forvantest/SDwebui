package webui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application extends SpringBootServletInitializer {

	@SuppressWarnings("resource")
	public static void main(final String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

		long time1, time2;

		time1 = System.currentTimeMillis();
		mission(context.getBean(WorkService.class));
		time2 = System.currentTimeMillis();
		System.out.println("mission 花了：" + (time2 - time1) / 1000 + "秒");

		System.out.println("\n\nok");
		System.exit(0);
	}

	private static void mission(WorkService work) {

		work.txt2img_main();

	}

}