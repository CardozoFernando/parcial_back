package ar.edu.utnfrc.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_employee_id")
    @SequenceGenerator(name = "seq_employee_id", sequenceName = "SEQ_EMPLOYEE_ID", allocationSize = 1)
    @Column(name = "EMPLOYEE_ID")
    private Integer id;
    
    @Column(name = "last_name")
    private String lastName;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "title")
    private String title;
    
    @ManyToOne
    @JoinColumn(name = "reports_to")
    private Employee manager;
    
    @Column(name = "birth_date")
    private LocalDate birthDate;
    
    @Column(name = "hire_date")
    private LocalDate hireDate;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "state")
    private String state;
    
    @Column(name = "country")
    private String country;
    
    @Column(name = "postal_code")
    private String postalCode;
    
    @Column(name = "phone")
    private String phone;
    
    @Column(name = "fax")
    private String fax;
    
    @Column(name = "email")
    private String email;
    
    @OneToMany(mappedBy = "supportRep")
    private Set<Customer> customers;
}