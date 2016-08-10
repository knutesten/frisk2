package no.mesan.dao.mappers;

import no.mesan.model.User;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements ResultSetMapper<User> {
    private final String idName;

    public UserMapper(){
        idName = "id";
    }
    UserMapper(String idName) {
        this.idName = idName;
    }

    @Override
    public User map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new User(
                resultSet.getInt(idName),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getString("email"),
                resultSet.getString("username")
        );
    }
}
