package samdasu.recipt;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import samdasu.recipt.domain.entity.User;
import samdasu.recipt.domain.repository.UserRepository;

import java.util.List;

@SpringBootTest
@Transactional
@Rollback(value = false)
class UserTest {
    @PersistenceContext
    EntityManager em;


    private final UserRepository userRepository;

    @Autowired
    UserTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Test
    public void testEntity() {
        User user1 = new User("user1", "id1", "pw1", 10);
        User user2 = new User("user2", "id2", "pw2", 20);
        em.persist(user1);
        em.persist(user2);

        em.flush();
        em.clear();

        List<User> users = em.createQuery("select u from User u", User.class)
                .getResultList();

        for (User user : users) {
            System.out.println("user.getUsername() = " + user.getUsername());
        }

    }
}