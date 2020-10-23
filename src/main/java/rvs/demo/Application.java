package rvs.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.support.RegistrationPolicy;
import rvs.demo.model.MemberBo;
import rvs.demo.service.MemberService;

@EnableMBeanExport(registration= RegistrationPolicy.IGNORE_EXISTING)
@SpringBootApplication
@ComponentScan({"rvs.demo"})
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {

        ConfigurableApplicationContext context =
                SpringApplication.run(Application.class, args); // 取得Spring Context

        MemberService memberService = context.getBean(MemberService.class);

        MemberBo bo1 = new MemberBo();
        bo1.setId(10001L);
        bo1.setAccount("bo1");
        bo1.setPassword("bo1");
        memberService.add(bo1);

//        MemberBo bo2 = memberService.get(10001L);
//        System.out.println(bo2);


    }

}