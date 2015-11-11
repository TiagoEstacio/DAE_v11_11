/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import dtos.ManagerDTO;
import entities.Event;
import entities.Manager;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ManagerBean {

    @PersistenceContext
    private EntityManager em;
    
    public void createManager (String username, String password, String name, String email){
        try {
            Manager m = new Manager (username, password, name, email);
            em.persist(m);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }
    /*
    public List<Manager> getAllManagers() {
        try {
            List<Manager> managers = (List<Manager>) em.createNamedQuery("getAllManagers").getResultList();
            return managers;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }
    */
     public void updateManager (Long id, String username, String password, String name, String email){
        try {
            Manager mUpdate = em.find(Manager.class, id);
            if (mUpdate == null){
                return;
            }
            mUpdate.setUsername(username);
            mUpdate.setPassword(password);
            mUpdate.setName(name);
            mUpdate.setEmail(email);
            em.merge(mUpdate);   
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
     }
     
     public void removeManager(Long id){
        try {
            Manager mRemove = em.find(Manager.class, id);
            if (mRemove == null){
                return;
            }
            em.remove(mRemove);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        } 
     }
     
    public void enrollManagerInEvent(Long idManager, Long idEvent){
        try {
            Manager m = em.find(Manager.class, idManager);
            Event e = em.find(Event.class, idEvent);
        
            e.addManager(m);
            m.addEvent(e);
        
            em.merge(e);
            em.merge(m);
  
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
     }
    /*
    public List<Event> getAllEventsOfManager(Manager currentManager) {
        try {
            List<Event> events = currentManager.getEvents();
            return events; 
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }
    */
    
    public List<Event> getAllEventsOfManager(long id) {
        try {
            
            System.out.println("ID: " + id);
            
            Manager man = em.find(Manager.class, id);
            
            if(man != null){
                System.out.println("MANAGER: " + man.getName());
            }
            
            return man.getEvents();
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }
    
    public List<ManagerDTO> getAllManagers() {
        try {
            List<Manager> managers = (List<Manager>) em.createNamedQuery("getAllManagers").getResultList();
            return managersToDTO(managers);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }
    
    private List<ManagerDTO> managersToDTO(List<Manager> managers) {
        List<ManagerDTO> dtos = new ArrayList<>();
        for (Manager c : managers) {
            dtos.add(new ManagerDTO(c.getId(), c.getName(), c.getEmail(), c.getPassword(), c.getUserName()));            
        }
        return dtos;
    }
    
}
