package fa.training.vivuspringboot.entities;

import java.time.ZonedDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.TimeZoneStorage;

@Data
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false, columnDefinition = "NVARCHAR(255)")
    private String name;

    @Column(columnDefinition = "NVARCHAR(500)")
    private String description;

    @TimeZoneStorage
    @Column(nullable = false, columnDefinition = "DATETIMEOFFSET")
    private ZonedDateTime insertedAt;

    @TimeZoneStorage
    @Column(nullable = false, columnDefinition = "DATETIMEOFFSET")
    private ZonedDateTime updatedAt;

    @TimeZoneStorage
    @Column(columnDefinition = "DATETIMEOFFSET")
    private ZonedDateTime deletedAt;

    @Column(nullable = false)
    private boolean active;
}