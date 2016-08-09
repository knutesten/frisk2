package no.mesan.dao;

import no.mesan.dao.mappers.UserMapper;
import no.mesan.model.User;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

import java.util.Optional;

@RegisterMapper(UserMapper.class)
public interface UserDao {
    @SqlQuery("SELECT * FROM \"user\" WHERE email = :email")
    @SingleValueResult
    Optional<User> getUserByEmail(@Bind("email") String email);
}
