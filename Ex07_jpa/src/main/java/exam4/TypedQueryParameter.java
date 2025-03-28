package exam4;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

public class TypedQueryParameter {

	public static void main(String[] args) {
		 EntityManagerFactory emf = Persistence.createEntityManagerFactory("JpaEx01");
	        EntityManager em = emf.createEntityManager();
	        
	        try {
	        	em.getTransaction().begin();
	        	
	        	TypedQuery<Member4> query =
	        			em.createQuery(" select m from Member4 m "
	        					+ " where m.name = :name"
	        					,Member4.class)
	        				.setParameter("name", "더조은");
	        	List<Member4> lsit = query.getResultList();
	        	
	        	em.getTransaction().commit();
	        } catch(Exception e) {
	        	e.printStackTrace();
	        }
	        	em.close();
	        	emf.close();
	}

}
