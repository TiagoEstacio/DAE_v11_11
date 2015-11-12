
package dtos;

import entities_rev1.Event;
import java.io.Serializable;
import java.util.List;

public class AttendantDTO extends UserDTO implements Serializable {
    
    //precisa de lista de eventos? e categorias?
    //private List<Event> events;
    
    public AttendantDTO() {
    }    
    
    public AttendantDTO(Long id, String username, String password, String name, String email) {
        super(id, username, password, name, email);   
    }
    
    @Override
    public void reset() {
        super.reset();
    }
    
}