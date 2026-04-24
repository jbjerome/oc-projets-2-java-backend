package com.chatop.back.auth.domain;

import com.chatop.back.user.domain.entity.User;

public interface TokenIssuer {

    String issue(User user);
}
