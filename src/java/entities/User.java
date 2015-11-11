/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


/**
* criar, consultar/listar, e remover um ou mais utilizadores participantes em determinado
evento (exemplo: remover utilizador participante externo1@email.com do evento1;
adicionar lista de utilizadores participantes via copy/paste dos seus ids ao evento1;
adicionar lista de utilizadores participantes via outra classificação predefinida, como por
exemplo, todos os participantes classificados como inscritos à UC de DAE), para que
possa manter atualizada a lista de participantes de determinado evento;
 */
@Entity
@Table(name = "USERS")
@NamedQueries({
    @NamedQuery(
        name="getAllUsers",
        query="SELECT us FROM User us ORDER BY us.id"
    )
})
public abstract class User implements Serializable {
  
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    
    @NotNull
    protected String name;
    
    @NotNull
    @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
            + "[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
            + "(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
            message = "{invalid.email}")
    protected String email;
    
    @NotNull
    protected String userName;
    
    @NotNull
    protected String password; 
    
    public User() {
    }

    public User(String name, String email, String userName, String password) {
        
        this.name = name;
        this.email = email;
        this.userName = userName;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "entities.User[id=" + id + "]: "+ name;
    }

}
