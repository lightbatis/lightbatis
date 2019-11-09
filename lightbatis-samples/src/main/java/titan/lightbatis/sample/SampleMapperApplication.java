package titan.lightbatis.sample;

import java.util.List;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import titan.lightbatis.annotations.Lightbatis;
import titan.lightbatis.sample.domain.Member;
import titan.lightbatis.sample.mapper.MemberMapper;

@Lightbatis()//basePackages = "titan.lightbatis.sample.mapper"
@SpringBootApplication
public class SampleMapperApplication implements CommandLineRunner {

	@Autowired
	private MemberMapper memberMapper = null;

	public static void main(String[] args) {
		SpringApplication.run(SampleMapperApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		List<Member> members = memberMapper.listMember();
		for (Member member : members) {
			System.out.println(member);
		}
	}

}
