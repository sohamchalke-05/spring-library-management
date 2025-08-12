package com.LibraryManagementHomework.LibraryManagement.services;

import com.LibraryManagementHomework.LibraryManagement.DTO.Signupdto;
import com.LibraryManagementHomework.LibraryManagement.DTO.UserDto;
import com.LibraryManagementHomework.LibraryManagement.entities.User;
import com.LibraryManagementHomework.LibraryManagement.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserDto signup(Signupdto signupdto) {
        Optional<User> user = userRepo.findByEmail(signupdto.getEmail());
        if(user.isPresent()){
            throw new BadCredentialsException("User with email already exits "+ signupdto.getEmail());
        }

        User tobesave = modelMapper.map(signupdto,User.class);
        tobesave.setPassword(passwordEncoder.encode(tobesave.getPassword()));

        User newuser = userRepo.save(tobesave);
        return modelMapper.map(newuser,UserDto.class);
    }

    public User save(User newUser) {
        return userRepo.save(newUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByEmail(username).orElseThrow(() -> new BadCredentialsException("User with email "+ username +" not found"));
    }

    public User findByEmail(String email) {
        return userRepo.findByEmail(email).orElse(null);
    }

    public User findById(Long userId) {
        return userRepo.findById(userId).orElseThrow(()->new UsernameNotFoundException("User with id " + userId + " not found"));
    }


}
