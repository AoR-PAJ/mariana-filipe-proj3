package aor.paj.dao;
import aor.paj.entity.UserEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class UserDao extends AbstratDao<UserEntity>{
    private static final long serialVersionUID = 1L;


    public UserDao() {
        super(UserEntity.class);
    }

    @PersistenceContext
    private EntityManager entityManager;

    public UserEntity findByToken(String token) {
        try {
            return entityManager.createQuery("SELECT u FROM UserEntity u WHERE u.token = :token", UserEntity.class)
                    .setParameter("token", token)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public UserEntity findUserByUsername(String username) {
        try {
            return (UserEntity) em.createNamedQuery("Utilizador.findUserByUsername").setParameter("username", username)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    public List<UserEntity> findDeletedUsers() {
        return em.createQuery("SELECT u FROM UserEntity u WHERE u.isDeleted = true", UserEntity.class).getResultList();
    }


}
