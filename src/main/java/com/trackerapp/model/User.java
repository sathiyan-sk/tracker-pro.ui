package com.trackerapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Full name is required")
    @Column(name = "full_name", nullable = false)
    private String fullName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Column(name = "password", nullable = false)
    private String password;
    
    @NotBlank(message = "Department is required")
    @Column(name = "department", nullable = false)
    private String department;
    
    @NotBlank(message = "Employee ID is required")
    @Column(name = "emp_id", nullable = false, unique = true)
    private String empId;
    
    @NotBlank(message = "Mobile number is required")
    @Column(name = "mobile_no", nullable = false)
    private String mobileNo;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role = Role.USER;
    
    @Column(name = "enabled")
    private boolean enabled = true;
    
    // Constructors
    public User() {}
    
    public User(String fullName, String email, String password, String department, 
                String empId, String mobileNo, Role role) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.department = department;
        this.empId = empId;
        this.mobileNo = mobileNo;
        this.role = role;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getEmpId() { return empId; }
    public void setEmpId(String empId) { this.empId = empId; }
    
    public String getMobileNo() { return mobileNo; }
    public void setMobileNo(String mobileNo) { this.mobileNo = mobileNo; }
    
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", empId='" + empId + '\'' +
                ", role=" + role +
                '}';
    }
}