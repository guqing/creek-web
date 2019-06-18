package xyz.guqing.authorization.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyUserDetails implements UserDetails {
    
    private String username;
    
    private String password;
    
    private List<SysRole> sysRoles;
    
    private List<SysPermission> permissions;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//        for (SysRole role : sysRoles) {
//            authorities.add(new SimpleGrantedAuthority(role.getName()));
//        }
        
        return authorities;
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
        return true;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public List<SysRole> getSysRoles() {
        return sysRoles;
    }
    
    public void setSysRoles(List<SysRole> sysRoles) {
        this.sysRoles = sysRoles;
    }
    
    public List<SysPermission> getPermissions() {
        return permissions;
    }
    
    public void setPermissions(List<SysPermission> permissions) {
        this.permissions = permissions;
    }
}
