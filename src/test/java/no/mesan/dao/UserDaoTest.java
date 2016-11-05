package no.mesan.dao;

import no.mesan.dao.DaoTestRule.DataSet;
import no.mesan.model.User;
import org.junit.Rule;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserDaoTest {
    @Rule
    public DaoTestRule<UserDao> daoTestRule = new DaoTestRule<>(UserDao.class);
    private UserDao dao = daoTestRule.getDao();

    @Test
    @DataSet({"user.yml"})
    public void getUserById_returnUserOnEmail() {
        Optional<User> user = dao.getUserByEmail("knut.neksa@gmail.com");
        assertTrue(user.isPresent());
        assertEquals(new User(1, "Knut Esten Melandsø", "Nekså", "knut.neksa@gmail.com", "knuffern"), user.get());

        Optional<User> nonExistingUser = dao.getUserByEmail("hest");
        assertFalse(nonExistingUser.isPresent());
    }

}