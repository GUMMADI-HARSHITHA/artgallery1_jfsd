package com.jfsd.artgallery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtRepository extends JpaRepository<art, Long> {
	
	
	    @Query("SELECT a FROM art a WHERE a.email = :email AND a.password = :password")
	    art findByEmailAndPassword(@Param("email") String email, @Param("password") String password);
	    
	    @Modifying
	    @Query("UPDATE art a SET a.email = :newEmail, a.password = :newPassword WHERE a.email = :email")
	    int updateUserDetails(@Param("email") String email, @Param("newEmail") String newEmail, @Param("newPassword") String newPassword);


}
