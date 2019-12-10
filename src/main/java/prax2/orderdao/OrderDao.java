package prax2.orderdao;

import prax2.orderpojo.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import prax2.orderpojo.Order;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Component
public class OrderDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Order saveOrderAndReturnOrder(Order o) {
        em.persist(o);
        em.flush();
        return o;
    }

    @Transactional
    public Order getOrder(Long id) {
        return em.find(Order.class, id);
    }

    @Transactional
    public List<Order> getAllOrders() {
        return em.createQuery("select o from Order o", Order.class).getResultList();
    }

    @Transactional
    public void deleteOrder(Long id) {
        em.remove(em.find(Order.class, id));
    }

    @Transactional
    public List<User> getAllUsers() {
        return em.createQuery("select o from User o", User.class).getResultList();
    }
    @Transactional
    public User getUser(String id) {
        return em.find(User.class, id);
    }

}


