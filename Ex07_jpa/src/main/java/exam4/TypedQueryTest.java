package exam4;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.*;

public class TypedQueryTest {
	EntityManagerFactory emf = Persistence.createEntityManagerFactory("JpaEx01");
	EntityManager em = emf.createEntityManager()
	
	try {
	TypedQuery<Member4> query = em.createQuery("select m from Member4 m order by m.name ",Member4.class);
	
	List<Member4> list = query.getResultList();
	
	em.getTransaction().commit();
	}catch(Exception e) {
		em.getTransaction().rollback();
		e.printStackTrace();

		em.close();
		emf.close();
	}
	
	
}

}
