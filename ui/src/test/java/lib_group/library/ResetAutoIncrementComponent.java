<<<<<<< HEAD
package lib_group.library;

import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Component
public class ResetAutoIncrementComponent {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void autoIncrementTo1() {
        em.createNativeQuery("ALTER TABLE book AUTO_INCREMENT = 1;").executeUpdate();
        em.createNativeQuery("ALTER TABLE author AUTO_INCREMENT = 1;").executeUpdate();
        em.createNativeQuery("ALTER TABLE location AUTO_INCREMENT = 1;").executeUpdate();
        em.createNativeQuery("ALTER TABLE publish_house AUTO_INCREMENT = 1;").executeUpdate();
    }
}
=======
package lib_group.library;

import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Component
public class ResetAutoIncrementComponent {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void autoIncrementTo1() {
        em.createNativeQuery("ALTER TABLE book AUTO_INCREMENT = 1;").executeUpdate();
        em.createNativeQuery("ALTER TABLE author AUTO_INCREMENT = 1;").executeUpdate();
        em.createNativeQuery("ALTER TABLE location AUTO_INCREMENT = 1;").executeUpdate();
        em.createNativeQuery("ALTER TABLE publish_house AUTO_INCREMENT = 1;").executeUpdate();
    }
}
>>>>>>> 81f6070450e2b4cd442ec82d43bd5516a9882ea1
