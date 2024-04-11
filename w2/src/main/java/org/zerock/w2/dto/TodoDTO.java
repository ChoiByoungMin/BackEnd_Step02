package org.zerock.w2.dto;

import lombok.*;

import java.time.LocalDate;

@Builder            // 빌더 패턴 사용. 초기화시 필드초기화 처럼 가독성 증가 효과
@Data               // getter/setter/toString/equals/hashCode모두 오버라이딩
@NoArgsConstructor  // 매개변수 없는 생성자
@AllArgsConstructor // 모든 필드를 초기화 할 수 있는 생성자
public class TodoDTO {
    private  Long tno;
    private  String title;
    private LocalDate dueDate;
    private  boolean finished;
}
