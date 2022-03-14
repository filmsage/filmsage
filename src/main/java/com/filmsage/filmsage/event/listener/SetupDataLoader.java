package com.filmsage.filmsage.event.listener;

import com.filmsage.filmsage.models.User;
import com.filmsage.filmsage.models.UserContent;
import com.filmsage.filmsage.models.auth.Privilege;
import com.filmsage.filmsage.models.auth.Role;
import com.filmsage.filmsage.properties.AdminAccountProperties;
import com.filmsage.filmsage.repositories.UserRepository;
import com.filmsage.filmsage.repositories.auth.PrivilegeRepository;
import com.filmsage.filmsage.repositories.auth.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class SetupDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    private UserRepository userDao;

    private RoleRepository roleDao;

    private PrivilegeRepository privilegeDao;

    private PasswordEncoder passwordEncoder;

    private AdminAccountProperties adminProperties;

    public SetupDataLoader(UserRepository userDao,
                           RoleRepository roleDao,
                           PrivilegeRepository privilegeDao,
                           PasswordEncoder passwordEncoder,
                           AdminAccountProperties adminProperties) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.privilegeDao = privilegeDao;
        this.passwordEncoder = passwordEncoder;
        this.adminProperties = adminProperties;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;

        Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
        Privilege writeSelfPrivilege = createPrivilegeIfNotFound("WRITESELF_PRIVILEGE");
        Privilege deletePrivilege = createPrivilegeIfNotFound("DELETE_PRIVILEGE");
        Privilege deleteSelfPrivilege = createPrivilegeIfNotFound("DELETESELF_PRIVILEGE");
        Privilege updatePrivilege = createPrivilegeIfNotFound("UPDATE_PRIVILEGE");
        Privilege updateSelfPrivilege = createPrivilegeIfNotFound("UPDATESELF_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(
                readPrivilege,
                writePrivilege,
                deletePrivilege,
                updatePrivilege
        );

        List<Privilege> userPrivileges = Arrays.asList(
                readPrivilege,
                writeSelfPrivilege,
                deleteSelfPrivilege,
                updateSelfPrivilege
        );

        List<Privilege> guestPrivileges = Arrays.asList(
                readPrivilege
        );

        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", userPrivileges);
        createRoleIfNotFound("ROLE_GUEST", guestPrivileges);

        String testAdminUsername = "admin";
//      ------vvv TESTING ONLY vvv------
        if (!userDao.existsUserByUsername(testAdminUsername)) {
            Role adminRole = roleDao.findByName("ROLE_ADMIN");
            User user = new User();
            user.setPassword(passwordEncoder.encode(adminProperties.getPassword()));
            user.setEmail(adminProperties.getEmail());
            user.setUsername(adminProperties.getUsername());
            user.setRoles(Arrays.asList(adminRole));
            user.setEnabled(true);
            user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            userDao.save(user);
        }
//      ------^^^ TESTING ONLY ^^^------

        alreadySetup = true;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeDao.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeDao.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {

        Role role = roleDao.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleDao.save(role);
        }
        return role;
    }
}
