package com.example.springdonateweb.Repositories;

import com.example.springdonateweb.Models.Entities.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, Integer> {
    Optional<UsersEntity> findByEmail(String email);
//    Optional<UsersEntity> findByIdAndStatusTrue(Integer id);
    boolean existsByEmail(String email);
    List<UsersEntity> findByStatusTrue();
    Optional<UsersEntity> findByIdAndStatusTrue(int id);
//    boolean existsByIdAndStatusTrue(Integer id);
    Optional<UsersEntity> findByEmailAndStatusTrue(String email);


}
