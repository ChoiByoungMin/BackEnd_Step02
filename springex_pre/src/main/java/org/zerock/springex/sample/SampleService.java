package org.zerock.springex.sample;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@ToString
@RequiredArgsConstructor
public class SampleService {

    // lombok의 @RequiredArgsConstructor을 사용해서 '생성자 주입 방식'으로 객체를 주입한다.
    // 반드시 final로 선언해야 한다.

    @Qualifier("normal")
    private final SampleDAO sampleDAO;

    // 필드 주입 방식
    /*
    @Autowired
    private SampleDAO sampleDAO;
    */
}
