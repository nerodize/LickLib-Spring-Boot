package de.seitz.licklib.repository;

import de.seitz.licklib.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


// hier wird nur mit h2 getestet (in memory db)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepositoryTest;

    @Test
    @DisplayName("findByUsername: returns user if username exists")
    void findByUsername_exists_user() {
        // arrange (given)
        User user = new User(
                // uuid hier generiern ist fehlerhaft, da das schon in der entity von hibernate gemacht wird
                null,
                "alexx",
                "alex@gmail.com"
        );
        userRepositoryTest.save(user);

        //act (when)
        boolean exists = userRepositoryTest.findByUsername("alexy").isPresent();

        //assert (then)
        assertThat(exists).isTrue();

    }

    @Test
    void existsByEmail() {
    }
}