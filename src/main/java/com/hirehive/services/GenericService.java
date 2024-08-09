package com.hirehive.services;

import com.hirehive.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GenericService<T, ID>  {

    List<T> getAll();

    T getById(ID id);

    T create(T entity);

    T update(ID id, T entityDetails);

    void delete(ID id);


}
