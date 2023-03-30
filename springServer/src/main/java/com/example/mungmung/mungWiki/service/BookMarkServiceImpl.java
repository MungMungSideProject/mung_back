package com.example.mungmung.mungWiki.service;

import com.example.mungmung.memeber.entity.Member;
import com.example.mungmung.memeber.repository.MemberRepository;
import com.example.mungmung.mungWiki.entity.BookMark;
import com.example.mungmung.mungWiki.entity.DogType;
import com.example.mungmung.mungWiki.entity.MungWiki;
import com.example.mungmung.mungWiki.repository.BookMartRepository;
import com.example.mungmung.mungWiki.repository.MungWikiRepository;
import com.example.mungmung.security.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BookMarkServiceImpl implements BookMarkService{

    @Autowired
    private RedisService redisService;

    @Autowired
    private BookMartRepository bookMartRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MungWikiRepository mungWikiRepository;

    @Override
    public String bookMarkClick(String token,String dogType){
        DogType dogTypeValue = DogType.valueOfDogStatus(dogType);

        Long id = redisService.getValueByKey(token);
        Member member = memberRepository.findByMemberId(id);
        if(id.equals(null))return "there is an unusual approach";

        Optional<MungWiki> maybeMungWiki = mungWikiRepository.findByDogType(dogTypeValue);
        if(maybeMungWiki.isEmpty()) return "No wiki information";

        Optional<BookMark> maybeBookMark = bookMartRepository.findByMemberIDAndDogType(id,dogTypeValue);
        if(maybeBookMark.isEmpty()){
            BookMark bookMark = new BookMark();

            bookMark.setMarkFlag(true);
            bookMark.setMember(member);
            bookMark.setMungWiki(maybeMungWiki.get());

            bookMartRepository.save(bookMark);
        }else{
            BookMark bookMark = maybeBookMark.get();
            bookMark.setMarkFlag(!bookMark.getMarkFlag());

            bookMartRepository.save(bookMark);
        }

        return "book_mark success";
    }


}
