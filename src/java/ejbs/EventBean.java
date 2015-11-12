
package ejbs;

import dtos.AttendantDTO;
import dtos.CategoryDTO;
import dtos.EventDTO;
import dtos.ManagerDTO;
import entities.Attendant;
import entities.Category;
import entities.Event;
import entities.Manager;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityDoesNotExistsException;
import exceptions.MyConstraintViolationException;
import exceptions.Utils;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

@Stateless
public class EventBean {

    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private ManagerBean managerBean;
    @EJB
    private AttendantBean attendantBean;
    
    public void createEvent (String name, String description, String startDate, String finishDate)throws EntityAlreadyExistsException, MyConstraintViolationException {
        try {
            List<Event> events = (List<Event>) em.createNamedQuery("getAllEvents").getResultList();
            for (Event e : events){
                if (name.equals(e.getName())){
                    throw new EntityAlreadyExistsException("A event with that name already exists."); 
                }
            }
            Event event = new Event (name,description, startDate, finishDate);
            em.persist(event); 
        } catch (EntityAlreadyExistsException e) {
            throw e;           
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));            
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
     public List<EventDTO> getAllEvents() {
        try {
            List<Event> events = (List<Event>) em.createNamedQuery("getAllEvents").getResultList();
            return eventsToDTOs(events);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }
     
    public Event getEvent(String name) {
        try {
            Event event = new Event();
            List<Event> events = (List<Event>) em.createNamedQuery("getAllEvents").getResultList();
            for (Event e : events){
                if (name.equals(e.getName())){
                    event = e;
                    break;
                }
            }
            return event;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
 
    public void updateEvent(Long id, String name, String description, String startDate, String finishDate)throws EntityDoesNotExistsException, MyConstraintViolationException{
        try {
            Event event = em.find(Event.class, id);
            if (event == null){
                throw new EntityDoesNotExistsException("There is no event with that id.");
            }
            List<Event> events = (List<Event>) em.createNamedQuery("getAllEvents").getResultList();
            for (Event e : events){
                if (name.equals(e.getName())){
                    throw new EntityAlreadyExistsException("That event already exists.");
                }
            }
            event.setName(name);
            event.setDescription(description);
            event.setStartDate(startDate);
            event.setFinishDate(finishDate);
        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));            
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    public void removeEvent (Long id) throws EntityDoesNotExistsException {
        try {
            Event event = em.find(Event.class, id);
            if (event == null) {
                throw new EntityDoesNotExistsException("There is no event with that id.");
            }

            for (ManagerDTO manager : managerBean.getAllManagers()){
                managerBean.unrollManagerInEvent(manager.getId(),id);
            }
            
            for (AttendantDTO attendant : attendantBean.getAllAttendants()){
                attendantBean.unrollAttendantInEvent(attendant.getId(),id);
            }

            em.remove(event);
              
         } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public List<EventDTO> getManagerEvents(Long managerId) throws EntityDoesNotExistsException {
        try {
            Manager manager = em.find(Manager.class, managerId);
            if (manager == null) {
                throw new EntityDoesNotExistsException("Manager does not exists.");
            }
            return eventsToDTOs(manager.getEvents()); 
        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public List<EventDTO> getAttendantEvents(Long attendantId) throws EntityDoesNotExistsException {
        try {
            Attendant attendant = em.find(Attendant.class, attendantId);
            if (attendant == null) {
                throw new EntityDoesNotExistsException("Attendant does not exists.");
            }
            return eventsToDTOs(attendant.getEvents()); 
        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public List<EventDTO> getCategoryEvents(Long categoryId) throws EntityDoesNotExistsException {
        try {
            Category category = em.find(Category.class, categoryId);
            if (category == null) {
                throw new EntityDoesNotExistsException("Category does not exists.");
            }
            return eventsToDTOs(category.getEvents());   
        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }    
    
    EventDTO eventToDTO(Event event) {
        return new EventDTO(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getStartDate(),
                event.getFinishDate(),
                event.isOpenForEnroll());
    }
    
    List<EventDTO> eventsToDTOs(List<Event> events) {
        List<EventDTO> dtos = new ArrayList<>();
        for (Event e : events) {
            dtos.add(eventToDTO(e));
        }
        return dtos;
    }

}
