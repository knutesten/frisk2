package no.mesan.dao;

import com.google.common.collect.ImmutableList;
import no.mesan.dao.mappers.TypeMapper;
import no.mesan.model.Type;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(TypeMapper.class)
public interface TypeDao {
    @SqlQuery("select * from type")
    ImmutableList<Type> getTypes();
}
