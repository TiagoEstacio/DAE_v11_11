/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import entities.Category;
import entities.Event;
import entities.Attendant;
import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class AttendantBean {

    @PersistenceContext
    private EntityManager em;
    
    public void createAttendant (String name, String email, String userName, String password){
        try {
            Attendant p = new Attendant (name, email, userName, password);
            em.persist(p);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }
    
    public List<Attendant> getAllAttendants() {
        try {
            List<Attendant> attendants = (List<Attendant>) em.createNamedQuery("getAllAttendants").getResultList();
            return attendants;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }
 
     public void updateAttendant (Long id, String name, String email, String userName, String password){
        try {
            Attendant pUpdate = em.find(Attendant.class, id);
            if (pUpdate == null){
                return;
            }
            pUpdate.setName(name);
            pUpdate.setEmail(email);
            pUpdate.setUsername(userName);
            pUpdate.setPassword(password);
            em.merge(pUpdate);   
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
     }
     
     public void removeAttendant(Long id){
        try {
            Attendant pRemove = em.find(Attendant.class, id);
            if (pRemove == null){
                return;
            }
            em.remove(pRemove);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        } 
     }
     
    public void enrollAttendantInEvent(Long idAttendant, Long idEvent){
        try {
            Attendant a = em.find(Attendant.class, idAttendant);
            Event e = em.find(Event.class, idEvent);
        
            e.addAttendant(a);
            a.addEvent(e);
        
            em.merge(e);
            em.merge(a);
  
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
     }

    public List<Event> getAllEventsOfAttendant(Attendant currentAttendant) {
        try {
            List<Event> events = currentAttendant.getEvents();
            return events; 
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }
    
    public void enrollAttendantInCategory(Long idAttendant, Long idCategory){
        try {
            Attendant a = em.find(Attendant.class, idAttendant);
            Category c = em.find(Category.class, idCategory);
        
            c.addAttendant(a);
            a.addCategory(c);
        
            em.merge(c);
            em.merge(a);
  
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
     }

    public List<Category> getAllCategoriesOfAttendant(Attendant currentAttendant) {
        try {
            List<Category> categories = currentAttendant.getCategories();
            return categories; 
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public Attendant getAttendantByName(String  name) {
        for (Attendant att : getAllAttendants()) {
            if( att.getName().equals(name)){
                return att;
            }
                   
        }
        return null;
    }
}
