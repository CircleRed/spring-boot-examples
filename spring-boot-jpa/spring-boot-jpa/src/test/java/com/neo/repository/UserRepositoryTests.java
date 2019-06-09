package com.neo.repository;

import com.neo.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTests {

    @Resource
    private UserRepository userRepository;

    @Test
    public void testSave() {
        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
        String formattedDate = dateFormat.format(date);

        userRepository.save(new User("aa", "aa123456", "aa@126.com", "aa", formattedDate));
        userRepository.save(new User("bb", "bb123456", "bb@126.com", "bb", formattedDate));
        userRepository.save(new User("cc", "cc123456", "cc@126.com", "cc", formattedDate));

//		Assert.assertEquals(3, userRepository.findAll().size());
//		Assert.assertEquals("bb", userRepository.findByUserNameOrEmail("bb", "bb@126.com").getNickName());
//		userRepository.delete(userRepository.findByUserName("aa"));
    }


    @Test
    public void testBaseQuery() {
        //查找所有
        List<User> all = userRepository.findAll();
        System.out.println(all);

        //增加单个
        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
        String formattedDate = dateFormat.format(date);
        User user = new User("ff", "ff123456", "ff@126.com", "ff", formattedDate);
        user.setId(2L);
        userRepository.save(user);

        //userRepository.delete(user);


        //查找单个
        Optional<User> user1 = userRepository.findById(3L);
        System.out.println(userRepository.existsById(3L) + ": " + user1.get());

        //计数
        long count = userRepository.count();
        System.out.println(count);


    }

    @Test
    public void testCustomSql() {
        System.out.println(userRepository.modifyById("neo", 3L));
        //userRepository.deleteById(3L);
        System.out.println(userRepository.findByEmail("ff@126.com"));
    }


    @Test
    public void testPageQuery() {
        int page = 0;
        int size = 2;
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        System.out.println(userRepository.findALL(pageable).getContent());
        System.out.println("=================");
        System.out.println(userRepository.findByNickName("aa", pageable).getContent());
        System.out.println("=================");
        System.out.println(userRepository.findByNickNameLike("%a%", pageable).getContent());

    }

}