package com.tenco.blog_v2.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository // IoC
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    /**
     * 사용자 이름과 비밀번호로 사용자 조회
     * @param username
     * @param password
     * @return 조회된 User 엔티티, null
     */
    public User findByUsernameAndPassword(String username, String password) {

       TypedQuery<User> jpql =
               em.createQuery("select u from User u where u.username = :username and u.password = :password",User.class);
        jpql.setParameter("username", username);
        jpql.setParameter("password", password);
        return jpql.getSingleResult();
    }
}
