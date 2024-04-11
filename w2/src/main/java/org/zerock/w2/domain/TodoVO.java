package org.zerock.w2.domain;

import lombok.*;

import java.time.LocalDate;

/* Vo는 처음 객체가 생성될 때 생성자를 통해서 1번 초기화하면
더 이상 값을 변경하도록 만드는 의도

왜냐하면 이 Vo는 DB의 Table에서 값을 꺼내거나 저장시에 사용하기 때문에
혹시라도 모를 Table 데이터의 변경을 막기 위한 표현이다.

뒤에서 Vo는 다시 Dto로 변환시켜서 Service - Controller - View 계층으로 전달된다.
*

빌더 패턴 설명
https://inpa.tistory.com/entry/GOF-%F0%9F%92%A0-%EB%B9%8C%EB%8D%94Builder-%ED%8C%A8%ED%84%B4-%EB%81%9D%ED%8C%90%EC%99%95-%EC%A0%95%EB%A6%AC
* */

@Getter             // getter 메서드 no usages
@Builder            // 빌더 패턴, 생성자 초기화이지만 마치 필드 초기화
@ToString           // 필드 정보 보기
@NoArgsConstructor  // 매개변수 없는 생성자
@AllArgsConstructor // 모든 필드 생성자        ModelMapper로 Dto <-> Vo변환
public class TodoVO {
    private  Long tno;
    private  String title;
    private LocalDate dueDate;
    private  boolean finished;
}
