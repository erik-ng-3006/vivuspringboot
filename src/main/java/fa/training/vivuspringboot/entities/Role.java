package fa.training.vivuspringboot.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.Nationalized;

import java.util.Set;

@Entity
@Table(name = "roles")
public class Role extends MasterEntity{
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 500)
    @Nationalized
    private String description;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Role() {
    }

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
