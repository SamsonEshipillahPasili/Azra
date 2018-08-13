package com.azra.entities;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

@Entity
@Data
public class AzraUser implements Serializable, UserDetails{
    @Column
    private String name;
    @Column
    private String password;
    @Column
    private String role;
    @Column
    private Date dateJoined;
    @Column
    @Id
    private String username;
    @Column
    private String gender;
    @Column
    private String phoneNumber;
    @Column
    private boolean isEnabled;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    byte[] profileImage;
    @Column
    private boolean usernameUpdatable;

    public AzraUser(){
        this.dateJoined = new Date();
        this.isEnabled = true;
        this.usernameUpdatable = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role.equals("USER")){
            return AuthorityUtils.createAuthorityList("USER");
        }else if(role.equals("ADMIN")){
            return AuthorityUtils.createAuthorityList("ADMIN");
        }else{
            return AuthorityUtils.createAuthorityList("UNKNOWN");
        }
    }


    public String getFormatedDateJoined(){
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.format(this.dateJoined);
    }

    public String imagePath(){
        return "/img/" + username +"/";
    }

    public boolean isAdmin(){
        return this.role.equals("ADMIN");
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

}
