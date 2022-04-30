package com.example.classpresentation;

import com.example.classpresentation.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
//A Controller class that handles the http requests.

//specialized version of controller( controller + ResponseBody)
@RestController
public class UserController {
    private long id= 0;

    private HashMap<Long,User> usersMap;
    {
        usersMap = new HashMap<>();
        usersMap.put(++id,new User(id, "Bob"));
        usersMap.put(++id, new User(id, "Sunny"));
        usersMap.put(++id,new User(id,"Brooke"));
    }

    /*
       method: getUsers
       returns a list consisting of all User objects stored in the hash map.
       path: localhost:8080/users
     */
    @RequestMapping(value= "/users", method = RequestMethod.GET)
    public List<User>  getUsers() {
        ArrayList<User> list = new ArrayList<>();
        for(Map.Entry<Long,User> entry: usersMap.entrySet()){
            list.add(entry.getValue());
        }
        return list;
    }
        /*
           method: getName
              takes as input a long integer for ID and returns a string
              which is the name of the associated User object if the user exists,
              otherwise throws an exception.
              path URI example: users/0 - return the first user
         */

    @RequestMapping(value="/users/{id}", method= RequestMethod.GET)
    public String getName( @PathVariable long id){

        if(usersMap.containsKey(id)){
            return usersMap.get(id).getName();
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("id %d was not found", id));
        }
    }

       /*
       method: createUser
            takes a single User object as input and adds the object to the hash map
            here we allow the user to choose the ID,
            but must check whether the ID chosen by the user already exists
        */

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public String createUser(@RequestBody User user){

        if(!usersMap.containsKey(user.getId())) {
            usersMap.put(user.getId(), user);
            //user chooses id
            return String.format("user object with id %d was created successfully",user.getId());
        }
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Id already exists");

    }
        /*
           method: createUsers
           takes a list of User objects as input and
           adds to the hash map only those whose ID has not been chosen

         */

    @RequestMapping(value = "/users/list", method = RequestMethod.POST)
    public String createUsers(@RequestBody List<User> users){
        String added = "";      //place holders
        String notAdded = "";
        for(User u:users){

            if(!usersMap.containsKey(u.getId())){
                usersMap.put(u.getId(),u);


                added += String.format("users object with id %d was added successfully%n",u.getId());

            }
            else{
                notAdded += String.format("users object with id %d couldn't be added successfully%n",u.getId());
            }
        }
        if(notAdded.isEmpty())
            return added;
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,notAdded);
    }
    /*
       method: updateUser
        If the user exists in the hash map
        the method should update the user’s name with the name field of the input User object
     */
    @RequestMapping(value = "/users/update", method = RequestMethod.PUT)
    public String updateUser(@RequestBody User user){

        if(usersMap.containsKey(user.getId())){

            usersMap.replace(user.getId(),user);
            usersMap.get(user.getId()).setName(user.getName());

            return ("User on id " +user.getId()+ " replaced  successfully");
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("couldn't update user", id));
        }
    }
    /*
       method: deleteUser
            that takes an ID as input and deletes the User object
            with the given ID from the hash map, if the user exists
     */
    @RequestMapping(value = "/users/delete/{id}", method = RequestMethod.DELETE)
    public String deleteUser(@PathVariable long id) {

        if(usersMap.containsKey(id)){
            usersMap.remove(id);
            return String.format("deleted id %d user",id);
        }
        else {   //user doesn't exist
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("user can't be deleted"));
        }

    }

}
