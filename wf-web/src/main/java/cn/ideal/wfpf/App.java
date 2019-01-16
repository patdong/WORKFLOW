package cn.ideal.wfpf;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"cn.ideal.wfpf", "cn.ideal.wf"})
@MapperScan(basePackages={"cn.ideal.wfpf.dao","cn.ideal.wf.dao"})
public class App {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(App.class, args);
	}

}