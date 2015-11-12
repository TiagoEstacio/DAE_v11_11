/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs_rev1;

import entities_rev1.Administrator;
import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Stateless
public class AdministratorBean {

    @PersistenceContext
    EntityManager em;
    
    public void createAdministrator (String name, String email, String userName, String password){
        try {
            Administrator admin = new Administrator (name, email, userName, password);
            em.persist(admin);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }
    
    public List<Administrator> getAllAdministrators() {
        try {
            List<Administrator> administrators = (List<Administrator>) em.createNamedQuery("getAllAdministrators").getResultList();
            return administrators;
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }
 
     public void updateAdministrator (Long id, String name, String email, String userName, String password){
        try {
            Administrator admUpdate = em.find(Administrator.class, id);
            if (admUpdate == null){
                return;
            }
            admUpdate.setName(name);
            admUpdate.setEmail(email);
            admUpdate.setUsername(userName);
            admUpdate.setPassword(password);
            em.merge(admUpdate);   
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
     }
     
     public void removeAdministrator(Long id){
        try {
            Administrator admRemove = em.find(Administrator.class, id);
            if (admRemove == null){
                return;
            }
            em.remove(admRemove);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        } 
     }
}