package com.example.Controllers;


import com.example.Entities.User;
import com.example.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.json.JSONException;
import org.json.JSONObject;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(method = RequestMethod.POST)
    public User create(@RequestBody User user){
        return userRepository.save(user);

    }

    @PostMapping("/auth")
    public User autentificar(@RequestBody String json) throws JSONException {
        JSONObject response = new JSONObject(json);
        String email= response.getString("email");
        String password=response.getString("password");

        User user=userRepository.findByEmail(email);

        if(user==null || !user.getPassword().equals(password)) {
            return null;
        }

        return user;

    }


}
