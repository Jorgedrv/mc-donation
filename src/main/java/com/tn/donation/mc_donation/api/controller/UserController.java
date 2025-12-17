package com.tn.donation.mc_donation.api.controller;

import com.tn.donation.mc_donation.api.dto.UserRequest;
import com.tn.donation.mc_donation.api.dto.UserResponse;
import com.tn.donation.mc_donation.api.mapper.UserMapper;
import com.tn.donation.mc_donation.application.user.DeleteUserService;
import com.tn.donation.mc_donation.application.user.GetUsersService;
import com.tn.donation.mc_donation.application.user.ListUsersService;
import com.tn.donation.mc_donation.application.user.UpdateUserService;
import com.tn.donation.mc_donation.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Users", description = "Users management API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final ListUsersService listUsersService;
    private final GetUsersService getUsersService;
    private final UpdateUserService updateUserService;
    private final DeleteUserService deleteUserService;

    @Operation(
            summary = "List all users",
            description = "Retrieves all registered users."
    )
    @ApiResponse(responseCode = "200", description = "User list retrieved successfully")
    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> listUsers() {
        List<UserResponse> response = listUsersService.findAll()
                .stream()
                .map(UserMapper::toUserResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get user by ID",
            description = "Retrieves user information using its ID."
    )
    @ApiResponse(responseCode = "200", description = "User retrieved successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        User user = getUsersService.findById(id);
        return ResponseEntity.ok(UserMapper.toUserResponse(user));
    }

    @Operation(
            summary = "Update a user",
            description = "Updates the user's"
    )
    @ApiResponse(responseCode = "200", description = "Donor updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    @ApiResponse(responseCode = "404", description = "Donor not found")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequest request) {

        User updated = updateUserService.update(id, request);
        return ResponseEntity.ok(UserMapper.toUserResponse(updated));
    }

    @Operation(
            summary = "Delete a user",
            description = "Deletes a user by ID."
    )
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        deleteUserService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
