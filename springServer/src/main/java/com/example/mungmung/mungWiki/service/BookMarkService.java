package com.example.mungmung.mungWiki.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface BookMarkService {

    String bookMarkClick(String token,String dogType);

}
