package com.transfert.transfert.Controllers;
import com.transfert.transfert.Dto.Response.UserByIdResponse;
import com.transfert.transfert.Dto.Response.UsersResponse;
import com.transfert.transfert.Entities.Users;
import com.transfert.transfert.Services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UsersController {
    private final UsersService usersService;


    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> body) {
        String oldToken = body.get("oldToken");
        return usersService.refreshToken(oldToken);
    }




    @GetMapping("")
    public ResponseEntity<?> getAllUsers() {
        return usersService.getAllUsers();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable("id") String id, @RequestBody Users user) {
        return usersService.updateUser(id, user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserByIdResponse> getUser(@PathVariable("id") String id) {
        return usersService.getUser(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") String id) {
        return usersService.deleteUser(id);
    }

}
