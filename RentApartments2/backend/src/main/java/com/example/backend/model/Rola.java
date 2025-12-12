package com.example.backend.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="roles")
public class Rola {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable= false,unique = true)
    private String rolename;

    @OneToMany(mappedBy="role", cascade = CascadeType.ALL)
    private List<UserRole> roles = new ArrayList<>();

    public Rola(){}

    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}

    public String getRolename(){return rolename;}
    public void setRolename(String rolename){this.rolename=rolename;}

    public List<UserRole> getRoles(){return roles;}
    public void setRoles(List<UserRole> roles){this.roles=roles;}


}
