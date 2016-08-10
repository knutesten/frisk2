package no.mesan.dao.mappers;

import no.mesan.model.Type;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

class TypeMapper implements ResultSetMapper<Type> {
    private final String idName;

    public TypeMapper() {
        idName = "id";
    }

    TypeMapper(String idName) {
        this.idName = idName;
    }

    @Override
    public Type map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new Type(
                resultSet.getInt(idName),
                resultSet.getString("name"),
                resultSet.getInt("amount")
        );
    }
}
