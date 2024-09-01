package com.hirehive.services;

import com.hirehive.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface GenericService<T, ID>  {

    List<T> getAll();

    T getById(ID id);

    T create(T entity) throws IOException;

    T update(ID id, T entityDetails);

    void delete(ID id);


}
