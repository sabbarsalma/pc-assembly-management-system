package com.example.commande_pc.database;

import com.example.commande_pc.Utils;
import com.example.commande_pc.entity.Administrator;
import com.example.commande_pc.entity.Assembler;
import com.example.commande_pc.entity.Requester;
import com.example.commande_pc.entity.Role;
import com.example.commande_pc.entity.StoreKeeper;
import com.example.commande_pc.entity.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

public class MemoryDataBaseHelper {
    private static final ArrayList<User> users = new ArrayList<>();

    private static final ArrayList<Role> roles = new ArrayList<>();

    static {
        roles.add(new Role(1,"administrator"));
        roles.add(new Role(2,"requester"));
        roles.add(new Role(3,"storekeeper"));
        roles.add(new Role(4,"assembler"));
        // Default demo credentials — change before any real use
        users.add(new StoreKeeper("Lafleur","John","storekeeper@gmail.com",3, Utils.hashPassword("changeme123"),new Date(),new Date()));
        users.add(new Assembler("Legault","Francois","assembleur@gmail.com",2, Utils.hashPassword("changeme123"),new Date(),new Date()));
        users.add(new Administrator("Lacasse","Jean","admin@gmail.com",1, Utils.hashPassword("changeme123"),new Date(),new Date()));
    }
    public static Role findRoleById(long id){
        return roles.stream().filter(role -> {
            return role.getId() == id;
        }).findFirst().orElse(null);
    }
    public static User findUserById(long id){
        return users.stream().filter(user -> {
            return user.getId() == id;
        }).findFirst().orElse(null);
    }
    public static Role findRoleByRoleName(String roleName){
        return roles.stream().filter(role -> role.getName().equals(roleName)).findFirst().orElse(null);
    }
    public static User findUserByCredentials(String email, String password){
        User authUser = users.stream().filter(user -> user.getEmail().equals(email)).findFirst().orElse(null);
        if(authUser == null){
            return null;
        }
        if(Utils.checkPassword(authUser.getPassword(), password)){
            return authUser;
        }
        return null;
    }
    public static void addRequester(String lastName, String firstName, String email, String password){
        users.add(new Requester(lastName,firstName,email,users.size() + 1,Utils.hashPassword(password),new Date(),new Date()));
    }
    public static ArrayList<User> getRequesters(){
        return users.stream().filter(user -> user.getRoleId() == 2).collect(Collectors.toCollection(ArrayList::new));
    }
    public static void deleteRequester(long requester_id){
        users.removeIf(user -> user.getId() == requester_id);
    }
}
/**
 String password = "1234";
 String bcryptHashString = BCrypt.withDefaults().hashToString(12, password.toCharArray());
 // $2a$12$US00g/uMhoSBm.HiuieBjeMtoN69SN.GE25fCpldebzkryUyopws6
 ...
 BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), bcryptHashString);
 // result.verified == true
 */