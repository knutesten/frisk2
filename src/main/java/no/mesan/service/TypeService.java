package no.mesan.service;

import com.google.common.collect.ImmutableList;
import no.mesan.dao.TypeDao;
import no.mesan.model.Type;

public class TypeService {
    private TypeDao typeDao;

    public TypeService(TypeDao typeDao) {
        this.typeDao = typeDao;
    }

    public ImmutableList<Type> getTypes() {
        return typeDao.getTypes();
    }
}
