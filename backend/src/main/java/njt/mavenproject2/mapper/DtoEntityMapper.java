/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package njt.mavenproject2.mapper;

/**
 *
 * @author Korisnik
 */
public interface DtoEntityMapper<T,E> {
    
    T toDo(E e);
    E toEntity(T t);
    
    
}
