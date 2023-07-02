package ru.clevertec.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetByFilter<E, T> {

    /**
     * Возвращает объект класса Page в котором находятся объекты класса Е подходящие
     * по условиям класса Т
     *
     * @param filter Объект класса T содержащий условия для поиска
     * @param pageable Объект класса Pageable содержащий в условия пагинации
     * @return Объект класса Page содержащий объекты класса Е
     */
    Page<E> getByFilter(T filter, Pageable pageable);
}
