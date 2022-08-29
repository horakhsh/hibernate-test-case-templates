package org.hibernate.bugs;

import com.sanadpardaz.serp.entity.PersonalActor;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	@Test
	public void jpaSelectListCaseTest() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(new PersonalActor("a1", "a2"));
		entityManager.persist(new PersonalActor("a1", "b2"));
		entityManager.persist(new PersonalActor("c1", "c2"));
		entityManager.getTransaction().commit();
        List<?> actors = entityManager.createQuery("select case when pa.name = 'a1' then pa else pa end from PersonalActor pa ")
            .getResultList();
		entityManager.close();
        if(actors.get(0).getClass().isArray() && ((Object[])actors.get(0)).length == 0) {
            throw new Exception("error in handling case expressions with entity values. the result of jpa query with single (case exp) item select clause is a list of empty arrays. it must be a list of entity Objects ");
        }
	}

}
