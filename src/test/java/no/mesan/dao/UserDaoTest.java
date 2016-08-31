package no.mesan.dao;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserDaoTest extends DaoTest<UserDao>{
    @Before
    public void before() {
        initDataSet("user.yml");
    }

    @Test
    public void test() {
        System.out.println(dao.getUserByEmail("knut.neksa@gmail.com"));
    }
}