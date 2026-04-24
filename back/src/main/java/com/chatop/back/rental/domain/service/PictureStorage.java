package com.chatop.back.rental.domain.service;

import com.chatop.back.rental.domain.vo.PictureUpload;

public interface PictureStorage {

    /**
     * Stores the picture and returns a publicly resolvable URL.
     */
    String store(PictureUpload upload);
}
