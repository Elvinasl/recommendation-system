package services;

import java.util.List;

interface DatabaseServiceInterface<T> {


    void add(T o);

    void update(T o);

    List<T> list();

    T getById(int id);

    void remove(int id);


}
