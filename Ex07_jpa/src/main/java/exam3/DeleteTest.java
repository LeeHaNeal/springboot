package exam3;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DeleteTest {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("JpaEx01");
		EntityManager em = emf.createEntityManager();

		
		try {
			em.getTransaction().begin();
			
			Member3 user = em.find(Member3.class,"text@text.com");
			if(user ==null) {
				System.out.println("존재하지 않는 아이디 입니다.");
				return;
			}
			em.remove(user);
			
			
			System.out.println("탈퇴처리 되었습니다");
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
		
		
		
		
		
	}

}
