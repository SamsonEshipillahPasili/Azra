package com.azra.repositories;

import com.azra.entities.AzraUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<AzraUser, String> {
    @Query("SELECT u FROM AzraUser u")
    List<AzraUser> findAllUsers();
}
