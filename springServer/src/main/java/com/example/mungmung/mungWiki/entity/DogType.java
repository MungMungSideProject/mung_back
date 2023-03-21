package com.example.mungmung.mungWiki.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum DogType {
    POODLE("푸들"),
    SHIH_TZU("시츄"),
    BICHON_FRIZE("비숑프리제"),
    POMERANIAN("포메라니안"),
    MALTESE("말티즈"),
    YORKSHIRE_TERRIER("요크셔테리어"),
    CHIHUAHUA("치와와"),
    DACHSHUND("닥스훈트"),
    GOLDEN_RETRIEVER("골든리트리버"),
    LABRADOR_RETRIEVER("래브라도리트리버"),
    SIBERIAN_HUSKY("시베리안허스키"),
    ALASKAN_MALAMUTE("알래스칸말라뮤트"),
    SAMOYED("사모예드"),
    MINIATURE_SCHNAUZER("미니어쳐슈나우저"),
    BICHON("비숑"),
    BOSTON_TERRIER("보스턴 테리어"),
    BEAGLE("비글"),
    SHETLAND_SHEEPDOG("셰틀랜드 쉽독"),
    STANDARD_POODLE("스탠더드 푸들"),
    SHIBA("시바"),
    WELSH_CORGI("윌시 코기"),
    JINDO("진돗개"),
    GREYHOUND("그레이 하운드"),
    AMERICAN_COCKER_SPANIEL("아메리칸 코카스파니엘"),
    SPITZ("스피치"),
    BULLDOG("불도그"),
    MIX("믹스"),
    ETC("기타");

    final private String dogType;

    public static DogType valueOfPaymentState(String dogType) {
        return Arrays.stream(values())
                .filter(value -> value.dogType.equals(dogType))
                .findAny()
                .orElse(null);
    }

}
