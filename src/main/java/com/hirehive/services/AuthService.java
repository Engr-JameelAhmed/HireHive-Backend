package com.hirehive.services;

import com.hirehive.dto.LoginDto;

public interface AuthService {
    String login(LoginDto loginDto);
}
